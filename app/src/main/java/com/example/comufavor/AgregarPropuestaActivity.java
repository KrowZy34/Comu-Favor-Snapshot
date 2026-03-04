package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class AgregarPropuestaActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_propuesta);

        rootView = findViewById(R.id.agregarPropuestaMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupBottomNav();
        setupSubmitButton();
    }

    private void setupSubmitButton() {
        EditText etTitle = findViewById(R.id.etTitle);
        EditText etLocation = findViewById(R.id.etLocation);
        EditText etPayment = findViewById(R.id.etPayment);
        Button btnSubmitJob = findViewById(R.id.btnSubmitJob);

        btnSubmitJob.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String payment = etPayment.getText().toString().trim();

            if (title.isEmpty() || location.isEmpty() || payment.isEmpty()) {
                showSnackbar("Por favor, llena los datos de título, lugar y pago.");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("EXTRA_TITLE", title);
            resultIntent.putExtra("EXTRA_LOCATION", location);
            resultIntent.putExtra("EXTRA_PAYMENT", payment);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> showSnackbar("Ya estás en la sección Rec & Post"));
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            Intent intent = new Intent(AgregarPropuestaActivity.this, FeedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
        findViewById(R.id.navGuardados).setOnClickListener(v -> showSnackbar("Nav: Guardados"));
        findViewById(R.id.navPerfil).setOnClickListener(v -> showSnackbar("Nav: Perfil"));
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(getResources().getColor(R.color.dark_background, getTheme()));
        snackbar.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
        snackbar.show();
    }
}
