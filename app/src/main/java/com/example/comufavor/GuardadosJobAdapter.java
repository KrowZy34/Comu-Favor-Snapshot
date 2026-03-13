package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GuardadosJobAdapter extends RecyclerView.Adapter<GuardadosJobAdapter.GuardadoViewHolder> {

    private List<Job> jobList;

    public GuardadosJobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public GuardadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guardado_job_card, parent, false);
        return new GuardadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuardadoViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle());
        holder.tvJobLocation.setText(job.getLocation());
        holder.tvJobPrice.setText(job.getPrice());
        holder.tvJobDate.setText(job.getDate());
        holder.tvJobRating.setText("4.5");
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class GuardadoViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate, tvJobRating;

        public GuardadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvJobRating = itemView.findViewById(R.id.tvJobRating);
        }
    }
}
