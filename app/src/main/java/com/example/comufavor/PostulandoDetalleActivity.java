package com.example.comufavor;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class PostulandoDetalleActivity extends AppCompatActivity {

    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_postulando_detalle);

        rootView = findViewById(R.id.detalleEmpleoMain);

        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSpannableText();
        setupPopup();
        setupBottomNav();
    }

    private void setupSpannableText() {
        TextView tvInfo = findViewById(R.id.tvInformacion);
        String baseText = "Se requiere personal de limpieza para local comercial de 54 metros cuadrados. Sus tareas principales serán el barrido, trapeado de pisos, limpieza de lunas y desinfección de los servicios higiénicos. Se proporcionarán todos los implementos de limpieza necesarios. Horario flexible, coordinar disponibilidad. ";
        String masText = "mas";

        SpannableString spannableString = new SpannableString(baseText + masText);

        int greenColor = getResources().getColor(R.color.green_accent, getTheme());
        int startPos = baseText.length();
        int endPos = startPos + masText.length();

        spannableString.setSpan(new ForegroundColorSpan(greenColor), startPos, endPos,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvInfo.setText(spannableString);
    }

    private void setupPopup() {
        ImageView ivOptions = findViewById(R.id.ivMoreOptions);
        ivOptions.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(this).inflate(R.layout.layout_job_popup_menu, null);

            float density = getResources().getDisplayMetrics().density;
            int width = (int) (140 * density);
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // Allow outside clicks to dismiss
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // Set clicks within popup
            popupView.findViewById(R.id.btnMenuReportar).setOnClickListener(pv -> {
                showSnackbar("Reportar empleo presionado");
                popupWindow.dismiss();
            });

            popupView.findViewById(R.id.btnMenuGuardar).setOnClickListener(pv -> {
                showSnackbar("Empleo guardado en favoritos");
                popupWindow.dismiss();
            });

            // Show below the dots icon, aligned to the right edge
            popupWindow.showAsDropDown(ivOptions, 0, (int) (8 * density), android.view.Gravity.END);
        });
    }

    private void setupBottomNav() {
        findViewById(R.id.navRecPost).setOnClickListener(v -> showSnackbar("Ya estás en la sección Rec & Post"));
        findViewById(R.id.navMensajes).setOnClickListener(v -> showSnackbar("Nav: Mensajes"));
        findViewById(R.id.navInicio).setOnClickListener(v -> {
            Intent intent = new Intent(PostulandoDetalleActivity.this, FeedActivity.class);
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
