package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private UserPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPrefs = new UserPreferences(this);

        setupWelcome();
        setupSiguienteButton();
        setupBackHandler();
    }

    private void setupWelcome() {
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        String email = userPrefs.getLoggedInEmail();
        if (email != null) {
            String name = userPrefs.getUserName(email);
            if (!name.isEmpty()) {
                tvWelcome.setText(getString(R.string.welcome_message, name));
            } else {
                tvWelcome.setText(getString(R.string.welcome_message, email));
            }
        }
    }

    private void setupSiguienteButton() {
        findViewById(R.id.btnSiguiente).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PaymentMethodsActivity.class);
            startActivity(intent);
        });
    }

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Prevent going back to login screen — minimize app instead
                moveTaskToBack(true);
            }
        });
    }
}
