package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.ViewHolder> {

    private List<Applicant> applicantList;
    private OnApplicantClickListener listener;

    public interface OnApplicantClickListener {
        void onApplicantClick(Applicant applicant);
    }

    public ApplicantAdapter(List<Applicant> applicantList, OnApplicantClickListener listener) {
        this.applicantList = applicantList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicant_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Applicant applicant = applicantList.get(position);
        holder.tvName.setText(applicant.getName());
        holder.tvRating.setText(String.valueOf(applicant.getRating()));
        holder.ivAvatar.setImageResource(applicant.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApplicantClick(applicant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicantList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvName, tvRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivApplicantAvatar);
            tvName = itemView.findViewById(R.id.tvApplicantName);
            tvRating = itemView.findViewById(R.id.tvApplicantRating);
        }
    }
}
