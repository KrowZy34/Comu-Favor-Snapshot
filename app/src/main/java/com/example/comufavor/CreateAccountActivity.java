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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etDni, etEmail, etPassword;
    private CheckBox cbTerms;
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

        etDni = findViewById(R.id.etDni);
        etEmail = findViewById(R.id.etCreateEmail);
        etPassword = findViewById(R.id.etCreatePassword);
        cbTerms = findViewById(R.id.cbTerms);

        setupAccederButton();
        setupAlreadyHaveAccountText();
    }

    private void setupAccederButton() {
        findViewById(R.id.btnCreateAcceder).setOnClickListener(v -> {
            String dni = etDni.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (dni.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showSnackbar(getString(R.string.error_empty_fields));
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showSnackbar(getString(R.string.error_invalid_email));
                return;
            }
            if (password.length() < 6) {
                showSnackbar(getString(R.string.error_password_short));
                return;
            }
            if (!cbTerms.isChecked()) {
                showSnackbar(getString(R.string.error_accept_terms));
                return;
            }
            if (userPrefs.userExists(email)) {
                showSnackbar(getString(R.string.error_email_exists));
                return;
            }

            userPrefs.saveUser(email, password, dni);
            userPrefs.setLoggedIn(email);

            showSnackbar(getString(R.string.success_account_created));

            // Navigate to Onboarding
            rootView.postDelayed(() -> {
                Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 800);
        });
    }

    private void setupAlreadyHaveAccountText() {
        TextView tvAlready = findViewById(R.id.tvCreateAlreadyAccount);

        String prefix = getString(R.string.already_have_account) + " ";
        String link = getString(R.string.acceder_link);
        String fullText = prefix + link;

        SpannableString spannable = new SpannableString(fullText);
        int start = prefix.length();
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
                ds.setColor(getResources().getColor(R.color.green_accent, getTheme()));
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
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}
