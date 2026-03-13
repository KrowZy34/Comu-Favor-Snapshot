package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class VerificationCodeActivity extends AppCompatActivity {

    private EditText[] codeInputs;
    private TextView tvCountdown, tvResendCode;
    private View rootView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verification_code);

        rootView = findViewById(R.id.verificationCodeMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupCodeInputs();
        setupButtons();
        startCountdown();
        setupBackArrow();
    }

    private void setupBackArrow() {
        findViewById(R.id.ivBack).setOnClickListener(v -> {
            finish();
        });
    }

    private void initViews() {
        codeInputs = new EditText[]{
                findViewById(R.id.etCode1),
                findViewById(R.id.etCode2),
                findViewById(R.id.etCode3),
                findViewById(R.id.etCode4),
                findViewById(R.id.etCode5),
                findViewById(R.id.etCode6)
        };
        tvCountdown = findViewById(R.id.tvCountdown);
        tvResendCode = findViewById(R.id.tvResendCode);
    }

    private void setupCodeInputs() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int index = i;
            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < codeInputs.length - 1) {
                        codeInputs[index + 1].requestFocus();
                    }
                }
            });

            // Handle backspace to move to previous field
            codeInputs[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && codeInputs[index].getText().toString().isEmpty()
                        && index > 0) {
                    codeInputs[index - 1].requestFocus();
                    codeInputs[index - 1].setText("");
                    return true;
                }
                return false;
            });
        }
    }

    private void setupButtons() {
        // Verificar button
        findViewById(R.id.btnVerificar).setOnClickListener(v -> {
            StringBuilder code = new StringBuilder();
            for (EditText input : codeInputs) {
                String digit = input.getText().toString().trim();
                if (digit.isEmpty()) {
                    showSnackbar(getString(R.string.verification_error_incomplete));
                    return;
                }
                code.append(digit);
            }

            // For this prototype, accept any complete 6-digit code
            showSnackbar(getString(R.string.verification_success));
            rootView.postDelayed(() -> {
                Intent intent = new Intent(VerificationCodeActivity.this, RoleSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 800);
        });

        // Mas informacion link
        findViewById(R.id.tvMoreInfo).setOnClickListener(v -> {
            Intent intent = new Intent(VerificationCodeActivity.this, VerificationInfoActivity.class);
            startActivity(intent);
        });

        // Reenviar código
        tvResendCode.setOnClickListener(v -> {
            if (tvResendCode.isEnabled()) {
                showSnackbar(getString(R.string.verification_resent));
                startCountdown();
            }
        });
    }

    private void startCountdown() {
        tvResendCode.setEnabled(false);
        tvResendCode.setAlpha(0.5f);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(55000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                tvCountdown.setText(getString(R.string.verification_countdown_format, seconds));
            }

            @Override
            public void onFinish() {
                tvCountdown.setText(getString(R.string.verification_countdown_ready));
                tvResendCode.setEnabled(true);
                tvResendCode.setAlpha(1.0f);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.purple_login, getTheme()));
        snackbar.show();
    }
}
