package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeJobAdapter extends RecyclerView.Adapter<EmployeeJobAdapter.EmployeeJobViewHolder> {

    private List<Job> jobList;

    public EmployeeJobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public EmployeeJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_employee_job_card, parent, false);
        return new EmployeeJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeJobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle());
        holder.tvJobLocation.setText(job.getLocation());
        holder.tvJobPrice.setText(job.getPrice());
        holder.tvJobDate.setText(job.getDate());
        // Rating is hardcoded for now, could be added to Job model later
        holder.tvJobRating.setText("4.5");
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class EmployeeJobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate, tvJobRating;

        public EmployeeJobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvJobRating = itemView.findViewById(R.id.tvJobRating);
        }
    }
}
