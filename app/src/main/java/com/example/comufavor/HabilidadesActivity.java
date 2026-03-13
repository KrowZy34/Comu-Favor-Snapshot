package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.util.Set;

public class HabilidadesActivity extends AppCompatActivity {

    private UserPreferences userPrefs;
    private String userEmail;
    private EditText etHabilidadInput;
    private EditText etDescripcionInput;
    private ChipGroup chipGroupHabilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_habilidades);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        userPrefs = new UserPreferences(this);
        userEmail = userPrefs.getLoggedInEmail();

        TextView tvSubtitleName = findViewById(R.id.tvSubtitleName);
        if (userEmail != null) {
            String firstName = userPrefs.getUserFirstName(userEmail);
            String lastName = userPrefs.getUserLastName(userEmail);
            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                tvSubtitleName.setText(firstName + " " + lastName);
            }
        }

        etHabilidadInput = findViewById(R.id.etHabilidadInput);
        etDescripcionInput = findViewById(R.id.etDescripcionInput);
        ImageView ivAdd = findViewById(R.id.ivAdd);
        chipGroupHabilidades = findViewById(R.id.chipGroupHabilidades);

        loadSkillsAndDescription();

        ivAdd.setOnClickListener(v -> addSkill());

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            if (userEmail != null) {
                String desc = etDescripcionInput.getText().toString();
                userPrefs.updateUserDescription(userEmail, desc);
            }
            Intent intent = new Intent(HabilidadesActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void addSkill() {
        String skill = etHabilidadInput.getText().toString().trim();
        if (!skill.isEmpty() && userEmail != null) {
            userPrefs.addSkill(userEmail, skill);
            addChipToGroup(skill);
            etHabilidadInput.setText("");
        }
    }

    private void loadSkillsAndDescription() {
        if (userEmail != null) {
            Set<String> skills = userPrefs.getSkills(userEmail);
            for (String skill : skills) {
                addChipToGroup(skill);
            }
            String desc = userPrefs.getUserDescription(userEmail);
            if (desc != null && !desc.isEmpty()) {
                etDescripcionInput.setText(desc);
            }
        }
    }

    private void addChipToGroup(String skillText) {
        TextView chip = new TextView(this);
        chip.setText(skillText);
        chip.setTextColor(getResources().getColor(R.color.black, getTheme()));
        chip.setTextSize(16);
        chip.setPadding(32, 16, 32, 16);
        chip.setBackgroundResource(R.drawable.bg_rounded_corner);
        chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getRandomChipColor()));

        // Add tag icon
        chip.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tag_outline, 0);
        chip.setCompoundDrawablePadding(16);

        chip.setOnClickListener(v -> {
            chipGroupHabilidades.removeView(chip);
            if (userEmail != null) {
                userPrefs.removeSkill(userEmail, skillText);
            }
        });

        chipGroupHabilidades.addView(chip);
    }

    private int getRandomChipColor() {
        int[] colors = {
                R.color.habilidad_chip_orange,
                R.color.habilidad_chip_purple,
                R.color.habilidad_chip_blue,
                R.color.habilidad_chip_red,
                R.color.habilidad_chip_green
        };
        int randomIndex = new java.util.Random().nextInt(colors.length);
        return getResources().getColor(colors[randomIndex], getTheme());
    }
}
