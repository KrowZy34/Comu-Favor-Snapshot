package com.example.comufavor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRemember;
    private UserPreferences userPrefs;
    private View rootView;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        userPrefs = new UserPreferences(this);

        // Auto-login if remembered
        String remembered = userPrefs.getRemembered();
        if (remembered != null && userPrefs.isLoggedIn()) {
            navigateToHome(remembered);
            return;
        }

        setContentView(R.layout.activity_main);
        rootView = findViewById(R.id.main);

        // Handle both system bars AND keyboard (IME) insets
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            // Use the larger of system bar bottom or IME bottom
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);

        setupLoginButton();
        setupCreateAccountText();
        setupForgotPassword();
        setupPasswordToggle();
    }

    private void setupLoginButton() {
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                showSnackbar(getString(R.string.error_empty_fields));
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackbar(getString(R.string.error_invalid_email));
                return;
            }

            if (userPrefs.validateLogin(email, password)) {
                userPrefs.setLoggedIn(email);
                if (cbRemember.isChecked()) {
                    userPrefs.setRemembered(email);
                } else {
                    userPrefs.clearRemembered();
                }
                navigateToHome(email);
            } else {
                showSnackbar(getString(R.string.error_invalid_credentials));
            }
        });
    }

    private void setupCreateAccountText() {
        TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);

        String fullText = getString(R.string.or_text) + " " + getString(R.string.create_account);
        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf(getString(R.string.create_account));
        int end = start + getString(R.string.create_account).length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.purple_login, getTheme()));
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCreateAccount.setText(spannable);
        tvCreateAccount.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreateAccount.setHighlightColor(Color.TRANSPARENT);
    }

    private void setupPasswordToggle() {
        ImageView ivToggle = findViewById(R.id.ivTogglePassword);
        ivToggle.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            if (isPasswordVisible) {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggle.setImageResource(R.drawable.ic_mostrar);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggle.setImageResource(R.drawable.ic_ocultar);
            }
            etPassword.setSelection(etPassword.getText().length());
        });
    }

    private void setupForgotPassword() {
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(v -> showRecoveryDialog());
    }

    private void showRecoveryDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_recovery_method, null);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.optionEmail).setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, VerificationCodeActivity.class);
            intent.putExtra("recovery_mode", "email");
            startActivity(intent);
        });

        dialogView.findViewById(R.id.optionSms).setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, VerificationCodeActivity.class);
            intent.putExtra("recovery_mode", "sms");
            startActivity(intent);
        });

        dialog.show();
    }

    private void navigateToHome(String email) {
        String role = userPrefs.getUserRole(email);
        Intent intent;
        
        if (role != null && !role.isEmpty()) {
            // Role selected, show welcome screen
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            // No role selected yet
            intent = new Intent(MainActivity.this, RoleSelectionActivity.class);
        }
        
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.purple_login, getTheme()));
        snackbar.show();
    }
}