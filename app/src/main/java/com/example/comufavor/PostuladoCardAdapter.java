package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostuladoCardAdapter extends RecyclerView.Adapter<PostuladoCardAdapter.PostuladoCardViewHolder> {

    private List<Job> jobList;

    public PostuladoCardAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public PostuladoCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_postulado_card, parent, false);
        return new PostuladoCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostuladoCardViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle());
        holder.tvJobLocation.setText(job.getLocation());
        holder.tvJobPrice.setText(job.getPrice());
        holder.tvJobDate.setText(job.getDate());
        holder.tvJobRating.setText("4.5");

        // Reset visibility
        holder.tvStatusText.setVisibility(View.GONE);
        holder.ivStatusIcon.setVisibility(View.GONE);
        holder.pbStatusSpinner.setVisibility(View.GONE);
        holder.ivStatusIcon.clearColorFilter();

        // Reset text constraint to anchor to icon by default
        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams textParams =
                (androidx.constraintlayout.widget.ConstraintLayout.LayoutParams) holder.tvStatusText.getLayoutParams();

        String status = job.getStatus();
        if (status == null || status.isEmpty()) {
            // Default state — grey border, no status
            holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_grey);
            return;
        }

        holder.tvStatusText.setVisibility(View.VISIBLE);

        switch (status) {
            case "confirmado":
                holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_green);
                holder.tvStatusText.setText("Confirmado");
                holder.tvStatusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.price_green));
                holder.ivStatusIcon.setVisibility(View.VISIBLE);
                holder.ivStatusIcon.setImageResource(R.drawable.ic_check_verde);
                // Anchor text to icon
                textParams.endToStart = R.id.ivStatusIcon;
                break;

            case "en_proceso":
                holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_orange);
                holder.tvStatusText.setText("Trabajo en proceso");
                holder.tvStatusText.setTextColor(0xFFFF9800); // orange
                holder.pbStatusSpinner.setVisibility(View.VISIBLE);
                holder.pbStatusSpinner.getIndeterminateDrawable()
                        .setColorFilter(0xFFFF9800, android.graphics.PorterDuff.Mode.SRC_IN);
                // Anchor text to spinner
                textParams.endToStart = R.id.pbStatusSpinner;
                break;

            case "propuesta_enviada":
                holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_grey);
                holder.tvStatusText.setText("Propuesta enviada");
                holder.tvStatusText.setTextColor(0xFF000000); // black
                holder.pbStatusSpinner.setVisibility(View.VISIBLE);
                holder.pbStatusSpinner.getIndeterminateDrawable()
                        .setColorFilter(0xFF555555, android.graphics.PorterDuff.Mode.SRC_IN);
                // Anchor text to spinner
                textParams.endToStart = R.id.pbStatusSpinner;
                break;

            case "finalizado":
                holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_red);
                holder.tvStatusText.setText("Trabajo finalizado");
                holder.tvStatusText.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.price_red));
                holder.ivStatusIcon.setVisibility(View.VISIBLE);
                holder.ivStatusIcon.setImageResource(R.drawable.ic_check_rojo);
                // Anchor text to icon
                textParams.endToStart = R.id.ivStatusIcon;
                break;

            default:
                holder.cardRoot.setBackgroundResource(R.drawable.bg_postulado_card_grey);
                holder.tvStatusText.setVisibility(View.GONE);
                break;
        }

        holder.tvStatusText.setLayoutParams(textParams);

        holder.cardRoot.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            android.content.Intent intent;
            if ("confirmado".equals(status)) {
                intent = new android.content.Intent(context, AcceptedJobDetailActivity.class);
            } else {
                intent = new android.content.Intent(context, JobDetailActivity.class);
            }

            intent.putExtra("job_title", job.getTitle());
            intent.putExtra("job_location", job.getLocation());
            // Make some have images randomly for testing based on position
            intent.putExtra("has_images", position % 2 == 0); 
            intent.putExtra("job_status", status);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    static class PostuladoCardViewHolder extends RecyclerView.ViewHolder {
        View cardRoot;
        TextView tvJobTitle, tvJobLocation, tvJobPrice, tvJobDate, tvJobRating, tvStatusText;
        ImageView ivStatusIcon;
        ProgressBar pbStatusSpinner;

        public PostuladoCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot = itemView.findViewById(R.id.cardRoot);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvJobLocation = itemView.findViewById(R.id.tvJobLocation);
            tvJobPrice = itemView.findViewById(R.id.tvJobPrice);
            tvJobDate = itemView.findViewById(R.id.tvJobDate);
            tvJobRating = itemView.findViewById(R.id.tvJobRating);
            tvStatusText = itemView.findViewById(R.id.tvStatusText);
            ivStatusIcon = itemView.findViewById(R.id.ivStatusIcon);
            pbStatusSpinner = itemView.findViewById(R.id.pbStatusSpinner);
        }
    }
}
