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
import android.widget.EditText;
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

    private EditText etNombres, etApellidos, etEdad, etCelular;
    private Spinner spinnerNacionalidad, spinnerCiudad, spinnerDistrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.formMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etEdad = findViewById(R.id.etEdad);
        etCelular = findViewById(R.id.etCelular);
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);
        spinnerDistrito = findViewById(R.id.spinnerDistrito);

        setupSpinners();
        setupAlreadyHaveAccountText();
        setupSiguienteButton();
    }

    private void setupSpinners() {
        String[] nacionalidades = { "Seleccionar...", "Peruana", "Colombiana", "Venezolana", "Ecuatoriana", "Boliviana",
                "Otra" };
        ArrayAdapter<String> adapterNac = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, nacionalidades);
        spinnerNacionalidad.setAdapter(adapterNac);

        String[] ciudades = { "Seleccionar...", "Lima", "Arequipa", "Trujillo", "Cusco", "Piura", "Otra" };
        ArrayAdapter<String> adapterCiu = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ciudades);
        spinnerCiudad.setAdapter(adapterCiu);

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
            // Validate fields
            String nombres = etNombres.getText().toString().trim();
            String apellidos = etApellidos.getText().toString().trim();
            String edad = etEdad.getText().toString().trim();
            String celular = etCelular.getText().toString().trim();
            String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
            String ciudad = spinnerCiudad.getSelectedItem().toString();
            String distrito = spinnerDistrito.getSelectedItem().toString();

            if (nombres.isEmpty() || apellidos.isEmpty() || edad.isEmpty() || celular.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            if (nacionalidad.equals("Seleccionar...") || ciudad.equals("Seleccionar...")
                    || distrito.equals("Seleccionar...")) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            // Pass form data to CreateAccountActivity
            Intent intent = new Intent(FormActivity.this, CreateAccountActivity.class);
            intent.putExtra("nombres", nombres);
            intent.putExtra("apellidos", apellidos);
            intent.putExtra("edad", edad);
            intent.putExtra("celular", celular);
            intent.putExtra("nacionalidad", nacionalidad);
            intent.putExtra("ciudad", ciudad);
            intent.putExtra("distrito", distrito);
            startActivity(intent);
        });
    }
}
