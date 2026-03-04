package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class PostuladosActivity extends AppCompatActivity {

    private View rootView;
    private View llAsignadoView;
    private ActivityResultLauncher<Intent> detalleLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postulados);

        rootView = findViewById(R.id.postuladosMain);
        llAsignadoView = findViewById(R.id.llAsignadoView);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupLauncher();
        setupAsignadoActions();
        setupRecyclerView();
        setupBottomNav();
    }

    private void setupLauncher() {
        detalleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String recruitedName = result.getData().getStringExtra("EXTRA_RECRUITED_NAME");
                        if (recruitedName != null) {
                            showAsignadoView(recruitedName);
                        }
                    }
                });
    }

    private void showAsignadoView(String name) {
        TextView tvNombre = findViewById(R.id.tvAsignadoNombre);
        tvNombre.setText(name);
        llAsignadoView.setVisibility(View.VISIBLE);
        showSnackbar("Candidato " + name + " reclutado exitosamente.");
    }

    private void setupAsignadoActions() {
        findViewById(R.id.btnAsignadoCancelar).setOnClickListener(v -> {
            llAsignadoView.setVisibility(View.GONE);
            showSnackbar("Asignación cancelada.");
        });
        findViewById(R.id.btnFinalizarTrabajo).setOnClickListener(v -> {
            showSnackbar("Finalizando trabajo con calificación...");
            llAsignadoView.setVisibility(View.GONE);
        });
    }

    private void setupRecyclerView() {
        RecyclerView rv = findViewById(R.id.rvPostulados);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Postulado> mockData = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mockData.add(new Postulado("Sandro Cha...", "3.5"));
        }

        PostuladoAdapter adapter = new PostuladoAdapter(mockData, postulado -> {
            Intent intent = new Intent(PostuladosActivity.this, PostulanteDetalleActivity.class);
            detalleLauncher.launch(intent);
        });
        rv.setAdapter(adapter);
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> showSnackbar("Ya estás en la sección Rec & Post"));
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            startActivity(new Intent(PostuladosActivity.this, FeedActivity.class));
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
