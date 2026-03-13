package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecEmployeeJobAdapter extends RecyclerView.Adapter<RecEmployeeJobAdapter.RecEmployeeJobViewHolder> {

    private List<Job> jobList;

    public RecEmployeeJobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public RecEmployeeJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rec_employee_job_card, parent, false);
        return new RecEmployeeJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecEmployeeJobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle());
        holder.tvJobLocation.setText(job.getLocation());
        holder.tvJobPrice.setText(job.getPrice());
        holder.tvJobDate.setText(job.getDate());
        holder.tvJobRating.setText("4.5");

        holder.itemView.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            android.content.Intent intent = new android.content.Intent(context, JobDetailActivity.class);
            intent.putExtra("job_title", job.getTitle());
            intent.putExtra("job_location", job.getLocation());
            intent.putExtra("job_price", job.getPrice());
            intent.putExtra("job_date", job.getDate());
            // No images available yet — photo section will be hidden
            intent.putExtra("has_images", false);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class RecEmployeeJobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate, tvJobRating;

        public RecEmployeeJobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvJobRating = itemView.findViewById(R.id.tvJobRating);
        }
    }
}
