package com.example.comufavor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etPhone, etEmail, etPassword;
    private CheckBox cbRemember;
    private UserPreferences userPrefs;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        rootView = findViewById(R.id.createAccountMain);

        // Handle both system bars AND keyboard (IME) insets
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottomPadding = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });

        userPrefs = new UserPreferences(this);

        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etCreateEmail);
        etPassword = findViewById(R.id.etCreatePassword);
        cbRemember = findViewById(R.id.cbRemember);

        setupAccederButton();
        setupAlreadyHaveAccountText();
    }

    private void setupAccederButton() {
        findViewById(R.id.btnCreateAcceder).setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showSnackbar(getString(R.string.error_empty_fields));
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackbar(getString(R.string.error_invalid_email));
                return;
            }
            if (!cbRemember.isChecked()) {
                showSnackbar(getString(R.string.error_accept_terms));
                return;
            }
            if (password.length() < 6) {
                showSnackbar(getString(R.string.error_password_short));
                return;
            }
            if (userPrefs.userExists(email)) {
                showSnackbar(getString(R.string.error_email_exists));
                return;
            }

            // Using phone instead of DNI for the registration parameters.
            userPrefs.saveUser(email, password, phone);

            // Note: cbRemember logic would go here if UserPreferences supports it,
            // e.g. userPrefs.setRememberMe(cbRemember.isChecked());
            userPrefs.setLoggedIn(email);

            showSnackbar(getString(R.string.success_account_created));

            // Navigate to Role Selection
            rootView.postDelayed(() -> {
                Intent intent = new Intent(CreateAccountActivity.this, RoleSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 800);
        });
    }

    private void setupAlreadyHaveAccountText() {
        TextView tvAlready = findViewById(R.id.tvCreateAlreadyAccount);

        String fullText = "o iniciar sesión";
        SpannableString spannable = new SpannableString(fullText);

        int start = 2; // "o " is length 2
        int end = fullText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.purple_login, getTheme()));
                ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAlready.setText(spannable);
        tvAlready.setMovementMethod(LinkMovementMethod.getInstance());
        tvAlready.setHighlightColor(Color.TRANSPARENT);
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.purple_login, getTheme()));
        snackbar.show();
    }
}
