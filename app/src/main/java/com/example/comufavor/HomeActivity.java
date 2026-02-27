package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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
        setupLogout();
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

    private void setupLogout() {
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            userPrefs.logout();
            userPrefs.clearRemembered();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to login screen — use logout instead
        moveTaskToBack(true);
    }
}
