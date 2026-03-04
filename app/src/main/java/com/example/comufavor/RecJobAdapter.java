package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecJobAdapter extends RecyclerView.Adapter<RecJobAdapter.ViewHolder> {
    private List<Job> jobList;

    public RecJobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rec_job_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvTitle.setText(job.getTitle());
        holder.tvLocation.setText(job.getLocation());
        holder.tvPrice.setText(job.getPrice());
        // Mock applicants count
        holder.tvApplicantsCount.setText("0000");

        holder.itemView.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            android.content.Intent intent = new android.content.Intent(context, PostuladosActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvPrice, tvApplicantsCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvLocation = itemView.findViewById(R.id.tvJobLocation);
            tvPrice = itemView.findViewById(R.id.tvJobPrice);
            tvApplicantsCount = itemView.findViewById(R.id.tvApplicantsCount);
        }
    }
}
