package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final String[] titles;
    private final String[] descriptions;
    private final String[] icons;

    public OnboardingAdapter(String[] titles, String[] descriptions, String[] icons) {
        this.titles = titles;
        this.descriptions = descriptions;
        this.icons = icons;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.tvTitle.setText(titles[position]);
        holder.tvDescription.setText(descriptions[position]);
        holder.tvIcon.setText(icons[position]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvIcon;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvOnboardingTitle);
            tvDescription = itemView.findViewById(R.id.tvOnboardingDesc);
            tvIcon = itemView.findViewById(R.id.tvOnboardingIcon);
        }
    }
}
