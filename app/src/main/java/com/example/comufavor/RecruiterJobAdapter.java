package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecruiterJobAdapter extends RecyclerView.Adapter<RecruiterJobAdapter.ViewHolder> {

    private List<RecruiterJob> jobList;
    private OnJobClickListener listener;

    public interface OnJobClickListener {
        void onJobClick(RecruiterJob job);
    }

    public RecruiterJobAdapter(List<RecruiterJob> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recruiter_home_job_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecruiterJob job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle());
        holder.tvJobLocation.setText(job.getLocation());
        holder.tvJobPrice.setText(job.getPrice());
        holder.tvJobDate.setText(job.getDate());
        
        if (job.getProposalsCount() > 0) {
            holder.itemView.setBackgroundResource(R.drawable.bg_job_card_proposals);
            holder.tvProposalsCount.setVisibility(View.VISIBLE);
            holder.tvProposalsCount.setText(job.getProposalsCount() + " Propuestas");
            
            // Return date to the left
            androidx.constraintlayout.widget.ConstraintSet constraintSet = new androidx.constraintlayout.widget.ConstraintSet();
            constraintSet.clone((androidx.constraintlayout.widget.ConstraintLayout) holder.itemView);
            constraintSet.clear(R.id.tvJobDate, androidx.constraintlayout.widget.ConstraintSet.END);
            constraintSet.connect(R.id.tvJobDate, androidx.constraintlayout.widget.ConstraintSet.START, androidx.constraintlayout.widget.ConstraintSet.PARENT_ID, androidx.constraintlayout.widget.ConstraintSet.START);
            constraintSet.applyTo((androidx.constraintlayout.widget.ConstraintLayout) holder.itemView);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_job_card_purple_border);
            holder.tvProposalsCount.setVisibility(View.GONE);
            
            // Move date to the right
            androidx.constraintlayout.widget.ConstraintSet constraintSet = new androidx.constraintlayout.widget.ConstraintSet();
            constraintSet.clone((androidx.constraintlayout.widget.ConstraintLayout) holder.itemView);
            constraintSet.clear(R.id.tvJobDate, androidx.constraintlayout.widget.ConstraintSet.START);
            constraintSet.connect(R.id.tvJobDate, androidx.constraintlayout.widget.ConstraintSet.END, androidx.constraintlayout.widget.ConstraintSet.PARENT_ID, androidx.constraintlayout.widget.ConstraintSet.END);
            constraintSet.applyTo((androidx.constraintlayout.widget.ConstraintLayout) holder.itemView);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate, tvProposalsCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvProposalsCount = itemView.findViewById(R.id.tvProposalsCount);
        }
    }
}
