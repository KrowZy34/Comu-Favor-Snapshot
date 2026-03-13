package com.example.comufavor;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout llHistoryContainer;
    private boolean isSelectionMode = false;
    private ImageView globalTrashIcon;
    private List<View> allRowViews = new ArrayList<>();
    private List<View> selectedRowViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.historyMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        llHistoryContainer = findViewById(R.id.llHistoryContainer);

        // Populate mockup data
        populateMockupData();
    }

    private void populateMockupData() {
        // Group "Hoy"
        addGroupHeader("Hoy - Miércoles 11 Marzo 2026", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", false);

        // Group "Ayer"
        addGroupHeader("Ayer - Miércoles 11 Marzo 2026", false);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", true);
        addItemRow("2:57", "Limpieza de local...", "S/000.00", false);
    }

    private void addGroupHeader(String title, boolean isFirst) {
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 24, 0, 8);
        headerLayout.setLayoutParams(layoutParams);

        TextView tvHeader = new TextView(this);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        tvHeader.setLayoutParams(textParams);
        tvHeader.setText(title);
        tvHeader.setTextColor(getResources().getColor(android.R.color.black));
        tvHeader.setTextSize(16);
        tvHeader.setTypeface(null, Typeface.BOLD);

        headerLayout.addView(tvHeader);

        if (isFirst) {
            globalTrashIcon = new ImageView(this);
            globalTrashIcon.setImageResource(R.drawable.ic_trash_outline_black);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(
                    (int)(24 * getResources().getDisplayMetrics().density),
                    (int)(24 * getResources().getDisplayMetrics().density));
            iconParams.setMarginEnd((int)(16 * getResources().getDisplayMetrics().density));
            globalTrashIcon.setLayoutParams(iconParams);
            globalTrashIcon.setVisibility(View.GONE);
            
            globalTrashIcon.setOnClickListener(v -> deleteSelectedRows());
            
            headerLayout.addView(globalTrashIcon);
        }

        llHistoryContainer.addView(headerLayout);
    }

    private void addItemRow(String time, String desc, String price, boolean isSuccess) {
        View rowView = LayoutInflater.from(this).inflate(R.layout.item_history_row, llHistoryContainer, false);

        TextView tvTime = rowView.findViewById(R.id.tvHistoryTime);
        TextView tvDesc = rowView.findViewById(R.id.tvHistoryTitle);
        TextView tvPrice = rowView.findViewById(R.id.tvHistoryPrice);
        ImageView ivStatus = rowView.findViewById(R.id.ivHistoryStatus);
        ImageView ivCheckbox = rowView.findViewById(R.id.ivHistoryCheckbox);

        LinearLayout llMainRow = rowView.findViewById(R.id.llMainRow);
        LinearLayout llExpandedContent = rowView.findViewById(R.id.llExpandedContent);

        tvTime.setText(time);
        tvDesc.setText(desc);
        tvPrice.setText(price);

        if (isSuccess) {
            ivStatus.setImageResource(R.drawable.ic_check_green);
        } else {
            ivStatus.setImageResource(R.drawable.ic_close_red);
        }

        allRowViews.add(rowView);

        llMainRow.setOnLongClickListener(v -> {
            if (!isSelectionMode) {
                startSelectionMode();
                toggleRowSelection(rowView, llMainRow, ivCheckbox);
                return true;
            }
            return false;
        });

        llMainRow.setOnClickListener(v -> {
            if (isSelectionMode) {
                toggleRowSelection(rowView, llMainRow, ivCheckbox);
            } else {
                boolean isExpanded = llExpandedContent.getVisibility() == View.VISIBLE;
                if (isExpanded) {
                    llExpandedContent.setVisibility(View.GONE);
                    tvTime.setTextColor(android.graphics.Color.parseColor("#333333"));
                    tvDesc.setTextColor(android.graphics.Color.parseColor("#333333"));
                    tvPrice.setTextColor(android.graphics.Color.parseColor("#333333"));
                } else {
                    llExpandedContent.setVisibility(View.VISIBLE);
                    tvTime.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                    tvDesc.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                    tvPrice.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                }
            }
        });

        llHistoryContainer.addView(rowView);
    }

    private void startSelectionMode() {
        isSelectionMode = true;
        if (globalTrashIcon != null) {
            globalTrashIcon.setVisibility(View.VISIBLE);
        }
        for (View rv : allRowViews) {
            ImageView cb = rv.findViewById(R.id.ivHistoryCheckbox);
            if (cb != null) cb.setVisibility(View.VISIBLE);
        }
        selectedRowViews.clear();
    }

    private void endSelectionMode() {
        isSelectionMode = false;
        if (globalTrashIcon != null) {
            globalTrashIcon.setVisibility(View.GONE);
        }
        for (View rv : allRowViews) {
            ImageView cb = rv.findViewById(R.id.ivHistoryCheckbox);
            if (cb != null) {
                cb.setVisibility(View.GONE);
                cb.setImageResource(R.drawable.ic_checkbox_unchecked);
            }
            LinearLayout mainRow = rv.findViewById(R.id.llMainRow);
            if (mainRow != null) {
                mainRow.setBackground(null);
            }
        }
        selectedRowViews.clear();
    }

    private void toggleRowSelection(View rowView, LinearLayout mainRow, ImageView checkbox) {
        if (selectedRowViews.contains(rowView)) {
            selectedRowViews.remove(rowView);
            checkbox.setImageResource(R.drawable.ic_checkbox_unchecked);
            mainRow.setBackground(null);
            
            if (selectedRowViews.isEmpty()) {
                endSelectionMode();
            }
        } else {
            selectedRowViews.add(rowView);
            checkbox.setImageResource(R.drawable.ic_checkbox_checked);
            mainRow.setBackgroundResource(R.drawable.bg_history_row_selected);
        }
    }

    private void deleteSelectedRows() {
        for (View rv : selectedRowViews) {
            llHistoryContainer.removeView(rv);
            allRowViews.remove(rv);
        }
        endSelectionMode();
    }
}
