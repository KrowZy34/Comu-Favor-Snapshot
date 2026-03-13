package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecruitedJobAdapter extends RecyclerView.Adapter<RecruitedJobAdapter.RecruitedJobViewHolder> {

    private List<String> titles;

    public RecruitedJobAdapter(List<String> titles) {
        this.titles = titles;
    }

    @NonNull
    @Override
    public RecruitedJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recruited_job, parent, false);
        return new RecruitedJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecruitedJobViewHolder holder, int position) {
        holder.tvRecruitedTitle.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles != null ? titles.size() : 0;
    }

    static class RecruitedJobViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecruitedTitle;

        public RecruitedJobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecruitedTitle = itemView.findViewById(R.id.tvRecruitedTitle);
        }
    }
}
