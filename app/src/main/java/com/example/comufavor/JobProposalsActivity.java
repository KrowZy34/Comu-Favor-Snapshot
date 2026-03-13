package com.example.comufavor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JobProposalsActivity extends AppCompatActivity {

    private TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate;
    private RecyclerView rvApplicants;
    private ApplicantAdapter adapter;
    private List<Applicant> applicantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_proposals);

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvJobLocation = findViewById(R.id.tvJobLocation);
        tvJobPrice = findViewById(R.id.tvJobPrice);
        tvJobDate = findViewById(R.id.tvJobDate);
        rvApplicants = findViewById(R.id.rvApplicants);
        rvApplicants.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.jobProposalsMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecruiterJob job = (RecruiterJob) getIntent().getSerializableExtra("SELECTED_JOB");
        if (job != null) {
            tvJobTitle.setText(job.getTitle());
            tvJobLocation.setText(job.getLocation());
            tvJobPrice.setText(job.getPrice());
            tvJobDate.setText(job.getDate());
        }

        setupMockApplicants();
    }

    private void setupMockApplicants() {
        applicantList = new ArrayList<>();
        String[] names = {
            "Sandro Rodrigo Chavez Barrios", "Carlos Ruiz", "Maria Garcia", 
            "Luis Torres", "Ana Martinez", "Jorge Lopez", 
            "Lucia Diaz", "Pedro Perez", "Elena Gomez", "Raul Castro"
        };
        
        String[] possibleSkills = {
            "Cambio de aceite", "Lavado de autos", "Estibador", "Repartidor", 
            "Volantero", "Mecánica básica", "Limpieza", "Pintura"
        };

        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            String name = names[random.nextInt(names.length)];
            double rating = 3.5 + (random.nextDouble() * 1.5);
            rating = Math.round(rating * 10.0) / 10.0;
            
            String price = "S/" + (50 + random.nextInt(150)) + ".00";
            String description = getString(R.string.mock_description);
            
            List<String> skills = new ArrayList<>();
            int skillsCount = 2 + random.nextInt(4);
            for (int j = 0; j < skillsCount; j++) {
                String skill = possibleSkills[random.nextInt(possibleSkills.length)];
                if (!skills.contains(skill)) skills.add(skill);
            }

            applicantList.add(new Applicant(name, rating, R.drawable.ic_nav_account, description, price, skills));
        }

        adapter = new ApplicantAdapter(applicantList, applicant -> {
            Intent intent = new Intent(this, ApplicantDetailActivity.class);
            intent.putExtra("SELECTED_APPLICANT", applicant);
            startActivity(intent);
        });
        rvApplicants.setAdapter(adapter);
    }
}
