package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class PostulanteDetalleActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postulante_detalle);

        rootView = findViewById(R.id.detalleMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSpannableText();
        setupActions();
        setupBottomNav();
    }

    private boolean isDescExpanded = false;
    private boolean isEspecialidadesExpanded = false;

    private void setupSpannableText() {
        setupDescriptionText();
        setupEspecialidadesText();
    }

    private void setupDescriptionText() {
        TextView tvDesc = findViewById(R.id.tvDescripcion);
        String fullText = "Soy Sandro Chavez, estudiante de Ingeniería de Software con IA en SENATI. Actualmente trabajo desarrollando y diseñando soluciones web a medida para empresas, donde también tengo la responsabilidad de liderar el equipo de desarrollo y gestionar la comunicación con los clientes. Me caracteriza mi capacidad de adaptación; antes del software, trabajé en mantenimiento automotriz, lo que me dio una base sólida en lógica y trabajo bajo presión. Ahora busco una oportunidad donde pueda seguir sumando experiencia en proyectos tecnológicos más ambiciosos.";
        int limit = 150;

        tvDesc.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        tvDesc.setHighlightColor(android.graphics.Color.TRANSPARENT);

        updateDescriptionView(tvDesc, fullText, limit);
    }

    private void updateDescriptionView(TextView tv, String fullText, int limit) {
        if (fullText.length() <= limit) {
            tv.setText(fullText);
            return;
        }

        String displayText;
        String actionText;
        if (isDescExpanded) {
            displayText = fullText + " ";
            actionText = "menos";
        } else {
            displayText = fullText.substring(0, limit) + "... ";
            actionText = "mas";
        }

        SpannableString spannable = new SpannableString(displayText + actionText);
        int greenColor = getResources().getColor(R.color.green_accent, getTheme());

        android.text.style.ClickableSpan clickableSpan = new android.text.style.ClickableSpan() {
            @Override
            public void onClick(@androidx.annotation.NonNull View widget) {
                isDescExpanded = !isDescExpanded;
                updateDescriptionView(tv, fullText, limit);
            }

            @Override
            public void updateDrawState(@androidx.annotation.NonNull android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(greenColor);
            }
        };

        spannable.setSpan(clickableSpan, displayText.length(), displayText.length() + actionText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);
    }

    private void setupEspecialidadesText() {
        TextView tvEspecialidades = findViewById(R.id.tvEspecialidades);
        java.util.List<String> especialidades = java.util.Arrays.asList("Análisis y Diseño web", "Desarrollo Backend",
                "Bases de Datos");

        tvEspecialidades.setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
        tvEspecialidades.setHighlightColor(android.graphics.Color.TRANSPARENT);

        updateEspecialidadesView(tvEspecialidades, especialidades);
    }

    private void updateEspecialidadesView(TextView tv, java.util.List<String> especialidades) {
        if (especialidades.size() <= 1) {
            tv.setText(especialidades.isEmpty() ? "" : especialidades.get(0));
            return;
        }

        String displayText;
        String actionText;
        if (isEspecialidadesExpanded) {
            displayText = String.join("\n", especialidades) + "\n";
            actionText = "ver menos";
        } else {
            displayText = especialidades.get(0) + " ";
            actionText = "+" + (especialidades.size() - 1) + " más";
        }

        SpannableString spannable = new SpannableString(displayText + actionText);
        int greenColor = getResources().getColor(R.color.green_accent, getTheme());

        android.text.style.ClickableSpan clickableSpan = new android.text.style.ClickableSpan() {
            @Override
            public void onClick(@androidx.annotation.NonNull View widget) {
                isEspecialidadesExpanded = !isEspecialidadesExpanded;
                updateEspecialidadesView(tv, especialidades);
            }

            @Override
            public void updateDrawState(@androidx.annotation.NonNull android.text.TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(greenColor);
                ds.setTextSize(getResources().getDisplayMetrics().scaledDensity * 16); // Normal font size for 'más'
                                                                                       // action
            }
        };

        spannable.setSpan(clickableSpan, displayText.length(), displayText.length() + actionText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannable);
    }

    private void setupActions() {
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnAceptar).setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("EXTRA_RECRUITED_NAME", "Sandro Chavez");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        findViewById(R.id.btnRechazar).setOnClickListener(v -> {
            showSnackbar("Postulante Rechazado");
            finish(); // Optional, but usually rejecting goes back
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> showSnackbar("Ya estás en la sección Rec & Post"));
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            Intent intent = new Intent(PostulanteDetalleActivity.this, FeedActivity.class);
            // Clear top to bring FeedActivity back
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
