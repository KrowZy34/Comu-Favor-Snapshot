package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RoleSelectionActivity extends AppCompatActivity {

    private UserPreferences userPrefs;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_role_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.roleSelectionMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userPrefs = new UserPreferences(this);
        userEmail = userPrefs.getLoggedInEmail();

        if (userEmail == null) {
            // Shouldn't happen unless called out of order, but fallback gracefully
            finish();
            return;
        }

        findViewById(R.id.cardReclutar).setOnClickListener(v -> selectRole("Reclutar"));
        findViewById(R.id.cardPostular).setOnClickListener(v -> selectRole("Postular"));
    }

    private void selectRole(String role) {
        userPrefs.updateUserRole(userEmail, role);

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
