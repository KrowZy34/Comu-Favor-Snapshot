package com.example.comufavor;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;

import java.util.Set;

public class ProfileHabilidadesActivity extends AppCompatActivity {

    private UserPreferences userPrefs;
    private String userEmail;
    private EditText etHabilidadInput;
    private EditText etDescripcionInput;
    private ChipGroup chipGroupHabilidades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_habilidades);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btnBack), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        userPrefs = new UserPreferences(this);
        userEmail = userPrefs.getLoggedInEmail();

        TextView tvSubtitleName = findViewById(R.id.tvSubtitleName);
        if (userEmail != null) {
            String firstName = userPrefs.getUserFirstName(userEmail);
            String lastName = userPrefs.getUserLastName(userEmail);
            if (!firstName.isEmpty() || !lastName.isEmpty()) {
                tvSubtitleName.setText(firstName + " " + lastName);
            } else {
                tvSubtitleName.setText("Usuario");
            }
        }

        etHabilidadInput = findViewById(R.id.etHabilidadInput);
        etDescripcionInput = findViewById(R.id.etDescripcionInput);
        TextInputLayout tilMisHabilidades = findViewById(R.id.tilMisHabilidades);
        chipGroupHabilidades = findViewById(R.id.chipGroupHabilidades);

        loadSkillsAndDescription();

        tilMisHabilidades.setEndIconOnClickListener(v -> addSkill());
        
        // Handle done action on keyboard
        etHabilidadInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                addSkill();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userEmail != null) {
            String desc = etDescripcionInput.getText().toString();
            userPrefs.updateUserDescription(userEmail, desc);
        }
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
        chip.setTextSize(14);
        chip.setPadding(
                dpToPx(12),
                dpToPx(8),
                dpToPx(12),
                dpToPx(8)
        );
        chip.setBackgroundResource(R.drawable.bg_chip_skill);
        chip.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getRandomChipColor()));
        
        // Add drawable end
        androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_tag_outline);
        chip.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tag_outline, 0);
        chip.setCompoundDrawablePadding(dpToPx(8));
        
        // Remove on click
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

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
