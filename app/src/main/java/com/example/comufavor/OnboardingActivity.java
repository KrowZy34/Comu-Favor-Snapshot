package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout dotsContainer;
    private Button btnNext;
    private OnboardingAdapter adapter;

    private final String[] titles = {
            "¡Bienvenido a ComuFavor!",
            "Encuentra oportunidades",
            "Conecta con tu comunidad",
            "Empieza a generar ingresos"
    };

    private final String[] descriptions = {
            "La plataforma diseñada para adultos mayores de 18 años que buscan generar ingresos extras ofreciendo servicios informales en su comunidad.",
            "Explora trabajos informales disponibles cerca de ti: limpieza, reparaciones, cuidado de mascotas, mandados, tutorías y mucho más.",
            "Facilita el contacto directo con clientes de tu zona. Sin intermediarios, sin complicaciones. Tú decides cuándo y cómo trabajar.",
            "Promueve tu autoempleo, la inclusión económica y el desarrollo dentro de tu comunidad. ¡Tu próximo trabajo está a un swipe de distancia!"
    };

    private final String[] icons = {
            "★",
            "🔍",
            "🤝",
            "💰"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);

        View rootView = findViewById(R.id.onboardingMain);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager = findViewById(R.id.viewPager);
        dotsContainer = findViewById(R.id.dotsContainer);
        btnNext = findViewById(R.id.btnOnboardingNext);

        adapter = new OnboardingAdapter(titles, descriptions, icons);
        viewPager.setAdapter(adapter);

        setupDots(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setupDots(position);
                if (position == adapter.getItemCount() - 1) {
                    btnNext.setText(R.string.btn_onboarding_start);
                } else {
                    btnNext.setText(R.string.btn_onboarding_next);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem();
            if (current < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(current + 1);
            } else {
                // Navigate to Home
                Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupDots(int currentPosition) {
        dotsContainer.removeAllViews();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            TextView dot = new TextView(this);
            dot.setText("●");
            dot.setTextSize(12);
            if (i == currentPosition) {
                dot.setTextColor(getResources().getColor(R.color.green_accent, getTheme()));
            } else {
                dot.setTextColor(0x80FFFFFF);
            }
            // Add spacing between dots
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsContainer.addView(dot);
        }
    }
}
