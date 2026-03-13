package com.example.comufavor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateJobActivity extends AppCompatActivity {

    private UserPreferences userPrefs;
    private JobPreferences jobPrefs;
    private TextView tvRecruiterName;
    private EditText etJobTitle, etJobLocation, etJobPrice, etJobDescription;
    private android.widget.LinearLayout llImagePreviewContainer;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_job);

        userPrefs = new UserPreferences(this);
        jobPrefs = new JobPreferences(this);
        tvRecruiterName = findViewById(R.id.tvRecruiterName);
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobLocation = findViewById(R.id.etJobLocation);
        etJobPrice = findViewById(R.id.etJobPrice);
        etJobDescription = findViewById(R.id.etJobDescription);
        llImagePreviewContainer = findViewById(R.id.llImagePreviewContainer);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createJobMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupLaunchers();
        setupRecruiterInfo();
        setupButtons();
        setupBottomNav();
    }

    private void setupLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        addImageToPreview(bitmap);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        addImageToPreview(uri);
                    }
                }
        );
    }

    private void addImageToPreview(Object imageSource) {
        ImageView iv = new ImageView(this);
        int size = (int) (60 * getResources().getDisplayMetrics().density);
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        
        android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(size, size);
        params.setMargins(0, 0, margin, 0);
        iv.setLayoutParams(params);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setBackgroundResource(R.drawable.bg_rounded_corner);
        iv.setClipToOutline(true);

        if (imageSource instanceof Bitmap) {
            iv.setImageBitmap((Bitmap) imageSource);
        } else if (imageSource instanceof Uri) {
            iv.setImageURI((Uri) imageSource);
        }

        llImagePreviewContainer.addView(iv);
    }

    private void setupRecruiterInfo() {
        String email = userPrefs.getLoggedInEmail();
        if (email != null) {
            String name = userPrefs.getUserName(email);
            tvRecruiterName.setText(name);
        }
    }

    private void setupButtons() {
        findViewById(R.id.btnDescartar).setOnClickListener(v -> {
            finish();
        });

        findViewById(R.id.btnEnviar).setOnClickListener(v -> {
            String title = etJobTitle.getText().toString().trim();
            String location = etJobLocation.getText().toString().trim();
            String price = etJobPrice.getText().toString().trim();
            String description = etJobDescription.getText().toString().trim();

            if (title.isEmpty() || location.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Por favor completa los campos principales", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            RecruiterJob newJob = new RecruiterJob(title, location, price, date, 0);
            jobPrefs.saveJob(newJob);

            Toast.makeText(this, "Trabajo publicado exitosamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        findViewById(R.id.ivAddFromCamera).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            } else {
                launchCamera();
            }
        });

        findViewById(R.id.ivAddFromGallery).setOnClickListener(v -> {
            galleryLauncher.launch("image/*");
        });
    }

    private void launchCamera() {
        cameraLauncher.launch(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
        }
    }

    private void setupBottomNav() {
        findViewById(R.id.navPublicados).setOnClickListener(v -> {
            startActivity(new Intent(this, PublishedJobsActivity.class));
        });

        findViewById(R.id.navPublicar).setOnClickListener(v -> {
            // Already here
        });

        findViewById(R.id.navInicio).setOnClickListener(v -> {
            Intent intent = new Intent(this, RecruiterHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navHistorial).setOnClickListener(v -> {
            startActivity(new Intent(this, HistoryActivity.class));
        });

        findViewById(R.id.navPerfil).setOnClickListener(v -> {
            // Navigate to profile
        });
    }
}
