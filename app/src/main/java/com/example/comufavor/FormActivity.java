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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSpinners();
        setupAlreadyHaveAccountText();
        setupSiguienteButton();
    }

    private void setupSpinners() {
        // Nacionalidad
        Spinner spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        String[] nacionalidades = { "Seleccionar...", "Peruana", "Colombiana", "Venezolana", "Ecuatoriana", "Boliviana",
                "Otra" };
        ArrayAdapter<String> adapterNac = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, nacionalidades);
        spinnerNacionalidad.setAdapter(adapterNac);

        // Ciudad
        Spinner spinnerCiudad = findViewById(R.id.spinnerCiudad);
        String[] ciudades = { "Seleccionar...", "Lima", "Arequipa", "Trujillo", "Cusco", "Piura", "Otra" };
        ArrayAdapter<String> adapterCiu = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ciudades);
        spinnerCiudad.setAdapter(adapterCiu);

        // Distrito
        Spinner spinnerDistrito = findViewById(R.id.spinnerDistrito);
        String[] distritos = { "Seleccionar...", "Miraflores", "San Isidro", "Surco", "Barranco", "San Borja", "Otro" };
        ArrayAdapter<String> adapterDis = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, distritos);
        spinnerDistrito.setAdapter(adapterDis);
    }

    private void setupAlreadyHaveAccountText() {
        TextView tvAlready = findViewById(R.id.tvAlreadyHaveAccount);

        String prefix = getString(R.string.already_have_account) + " ";
        String link = getString(R.string.acceder_link);
        String fullText = prefix + link;

        SpannableString spannable = new SpannableString(fullText);
        int start = prefix.length();
        int end = fullText.length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // Navigate back to login
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

    private void setupSiguienteButton() {
        findViewById(R.id.btnSiguiente).setOnClickListener(v -> {
            // Navigate to create account screen
            Intent intent = new Intent(FormActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }
}
