package com.example.comufavor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        userPrefs = new UserPreferences(this);

        // Auto-login if remembered
        String remembered = userPrefs.getRemembered();
        if (remembered != null && userPrefs.isLoggedIn()) {
            navigateToHome();
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
                navigateToHome();
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
                ds.setColor(getResources().getColor(R.color.green_accent, getTheme()));
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCreateAccount.setText(spannable);
        tvCreateAccount.setMovementMethod(LinkMovementMethod.getInstance());
        tvCreateAccount.setHighlightColor(Color.TRANSPARENT);
    }

    private void navigateToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}