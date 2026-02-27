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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRemember;
    private UserPreferences userPrefs;

    // Form data from previous screen
    private String nombres, apellidos, edad, celular, nacionalidad, ciudad, distrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createAccountMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPrefs = new UserPreferences(this);

        // Retrieve form data from Intent
        Intent data = getIntent();
        nombres = data.getStringExtra("nombres");
        apellidos = data.getStringExtra("apellidos");
        edad = data.getStringExtra("edad");
        celular = data.getStringExtra("celular");
        nacionalidad = data.getStringExtra("nacionalidad");
        ciudad = data.getStringExtra("ciudad");
        distrito = data.getStringExtra("distrito");

        etEmail = findViewById(R.id.etCreateEmail);
        etPassword = findViewById(R.id.etCreatePassword);
        cbRemember = findViewById(R.id.cbCreateRemember);

        setupAccederButton();
        setupAlreadyHaveAccountText();
    }

    private void setupAccederButton() {
        findViewById(R.id.btnCreateAcceder).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            // Validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, R.string.error_password_short, Toast.LENGTH_SHORT).show();
                return;
            }
            if (userPrefs.userExists(email)) {
                Toast.makeText(this, R.string.error_email_exists, Toast.LENGTH_SHORT).show();
                return;
            }

            // Save user
            userPrefs.saveUser(email, password,
                    nombres != null ? nombres : "",
                    apellidos != null ? apellidos : "",
                    edad != null ? edad : "",
                    celular != null ? celular : "",
                    nacionalidad != null ? nacionalidad : "",
                    ciudad != null ? ciudad : "",
                    distrito != null ? distrito : "");

            // Set session
            userPrefs.setLoggedIn(email);
            if (cbRemember.isChecked()) {
                userPrefs.setRemembered(email);
            }

            Toast.makeText(this, R.string.success_account_created, Toast.LENGTH_SHORT).show();

            // Navigate to Home
            Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
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
                // Go all the way back to login
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
}
