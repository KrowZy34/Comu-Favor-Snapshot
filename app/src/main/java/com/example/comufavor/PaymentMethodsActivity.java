package com.example.comufavor;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class PaymentMethodsActivity extends AppCompatActivity {

    private View rootView;
    private LinearLayout cardInputForm;
    private EditText etCardNumber, etExpiry, etCvv;
    private Button btnAddCard, btnSiguiente;
    private RadioButton rbYape, rbPaypal, rbMercadoPago, rbEfectivo;
    private RadioButton[] allRadios;
    private boolean cardAdded = false;
    private boolean isFormattingExpiry = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_methods);

        rootView = findViewById(R.id.paymentMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Card form
        cardInputForm = findViewById(R.id.cardInputForm);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvv = findViewById(R.id.etCvv);

        // Buttons
        btnAddCard = findViewById(R.id.btnAddCard);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        // Radio buttons
        rbYape = findViewById(R.id.rbYape);
        rbPaypal = findViewById(R.id.rbPaypal);
        rbMercadoPago = findViewById(R.id.rbMercadoPago);
        rbEfectivo = findViewById(R.id.rbEfectivo);
        allRadios = new RadioButton[] { rbYape, rbPaypal, rbMercadoPago, rbEfectivo };

        setupCardInputLimits();
        setupAddCardToggle();
        setupCardFieldWatchers();
        setupExpiryFormatter();
        setupRadioButtons();
        setupBottomButtons();
        setupSkip();
    }

    // ─── Input limits ───

    private void setupCardInputLimits() {
        // Card number: max 16 digits
        etCardNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(16) });
        etCardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

        // Expiry: max 5 chars (MM/YY)
        etExpiry.setFilters(new InputFilter[] { new InputFilter.LengthFilter(5) });
        etExpiry.setInputType(InputType.TYPE_CLASS_NUMBER);

        // CVV: max 4 digits
        etCvv.setFilters(new InputFilter[] { new InputFilter.LengthFilter(4) });
        etCvv.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    // ─── Toggle card form visibility ───

    private void setupAddCardToggle() {
        findViewById(R.id.rowAddCard).setOnClickListener(v -> {
            if (cardInputForm.getVisibility() == View.GONE) {
                cardInputForm.setVisibility(View.VISIBLE);
            } else {
                cardInputForm.setVisibility(View.GONE);
            }
        });
    }

    // ─── Card field watchers ───

    private void setupCardFieldWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Reset card-added state if user edits after adding
                if (cardAdded) {
                    cardAdded = false;
                    btnAddCard.setText(R.string.payment_btn_add_card);
                }

                // Mutual exclusivity: typing in card deselects radios
                clearAllRadios();

                updateAddCardButton();
                updateSiguienteButton();
            }
        };

        etCardNumber.addTextChangedListener(watcher);
        etCvv.addTextChangedListener(watcher);
        // Expiry uses its own formatter, which also calls the update logic
    }

    // ─── Expiry auto-format MM/YY ───

    private void setupExpiryFormatter() {
        etExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isFormattingExpiry)
                    return;
                isFormattingExpiry = true;

                // Strip non-digits
                String digits = s.toString().replaceAll("[^0-9]", "");

                // Limit to 4 digits (MMYY)
                if (digits.length() > 4) {
                    digits = digits.substring(0, 4);
                }

                // Auto-insert slash after MM
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digits.length(); i++) {
                    if (i == 2)
                        formatted.append('/');
                    formatted.append(digits.charAt(i));
                }

                etExpiry.setText(formatted.toString());
                etExpiry.setSelection(formatted.length());

                isFormattingExpiry = false;

                // Same update logic as other fields
                if (cardAdded) {
                    cardAdded = false;
                    btnAddCard.setText(R.string.payment_btn_add_card);
                }
                clearAllRadios();
                updateAddCardButton();
                updateSiguienteButton();
            }
        });
    }

    // ─── Card validation ───

    private boolean areCardFieldsFilled() {
        String number = etCardNumber.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();

        // Expiry: extract digits only
        String expiryDigits = etExpiry.getText().toString().replaceAll("[^0-9]", "");

        // Card: 13-16 digits, Expiry: 4 digits (MMYY), CVV: 3-4 digits
        return number.length() >= 13
                && expiryDigits.length() == 4
                && cvv.length() >= 3;
    }

    private void updateAddCardButton() {
        if (cardAdded) {
            btnAddCard.setEnabled(false);
            btnAddCard.setBackgroundResource(R.drawable.btn_login_border);
            btnAddCard.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
            return;
        }

        if (areCardFieldsFilled()) {
            btnAddCard.setEnabled(true);
            btnAddCard.setBackgroundResource(R.drawable.btn_login_border);
            btnAddCard.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        } else {
            btnAddCard.setEnabled(false);
            btnAddCard.setBackgroundResource(R.drawable.btn_disabled_border);
            btnAddCard.setTextColor(0xFF555555);
        }
    }

    // ─── Radio buttons — mutually exclusive with card ───

    private void setupRadioButtons() {
        int[] rowIds = { R.id.rowYape, R.id.rowPaypal, R.id.rowMercadoPago, R.id.rowEfectivo };

        for (int i = 0; i < rowIds.length; i++) {
            final RadioButton targetRb = allRadios[i];
            findViewById(rowIds[i]).setOnClickListener(v -> selectRadio(targetRb));
        }

        for (RadioButton rb : allRadios) {
            rb.setOnClickListener(v -> selectRadio(rb));
        }
    }

    private void selectRadio(RadioButton selected) {
        for (RadioButton rb : allRadios) {
            rb.setChecked(rb == selected);
        }

        // Mutual exclusivity: deselect card
        if (cardAdded) {
            cardAdded = false;
            clearCardForm();
            btnAddCard.setText(R.string.payment_btn_add_card);
            updateAddCardButton();
        }

        updateSiguienteButton();
    }

    private void clearAllRadios() {
        for (RadioButton rb : allRadios) {
            rb.setChecked(false);
        }
    }

    private boolean isAnyRadioSelected() {
        for (RadioButton rb : allRadios) {
            if (rb.isChecked())
                return true;
        }
        return false;
    }

    // ─── Card form helpers ───

    private void clearCardForm() {
        etCardNumber.setText("");
        etExpiry.setText("");
        etCvv.setText("");
        cardInputForm.setVisibility(View.GONE);
    }

    // ─── "Siguiente" button state ───

    private void updateSiguienteButton() {
        boolean enabled = cardAdded || isAnyRadioSelected();

        if (enabled) {
            btnSiguiente.setEnabled(true);
            btnSiguiente.setBackgroundResource(R.drawable.btn_login_border);
            btnSiguiente.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        } else {
            btnSiguiente.setEnabled(false);
            btnSiguiente.setBackgroundResource(R.drawable.btn_disabled_border);
            btnSiguiente.setTextColor(0xFF555555);
        }
    }

    // ─── Bottom buttons ───

    private void setupBottomButtons() {
        btnAddCard.setOnClickListener(v -> {
            clearAllRadios();

            cardAdded = true;
            btnAddCard.setText(R.string.payment_btn_card_added);
            btnAddCard.setEnabled(false);

            showSnackbar(getString(R.string.payment_selected, getString(R.string.payment_cards)));
            updateSiguienteButton();
        });

        btnSiguiente.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(PaymentMethodsActivity.this, FeedActivity.class);
            startActivity(intent);
        });
    }

    private void setupSkip() {
        findViewById(R.id.tvOmitir).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(PaymentMethodsActivity.this, FeedActivity.class);
            startActivity(intent);
        });
    }

    // ─── Snackbar ───

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}
