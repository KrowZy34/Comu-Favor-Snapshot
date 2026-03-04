package com.example.comufavor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostuladoAdapter extends RecyclerView.Adapter<PostuladoAdapter.ViewHolder> {
    public interface OnPostuladoClickListener {
        void onPostuladoClick(Postulado postulado);
    }

    private List<Postulado> postulados;
    private OnPostuladoClickListener listener;

    public PostuladoAdapter(List<Postulado> postulados, OnPostuladoClickListener listener) {
        this.postulados = postulados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_postulado, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Postulado p = postulados.get(position);
        holder.tvNombre.setText(p.getNombre());
        holder.tvCalificacion.setText(p.getCalificacion());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPostuladoClick(p);
            }
        });

        holder.ivGuardar.setOnClickListener(v -> {
            android.widget.Toast.makeText(v.getContext(), "Postulante guardado", android.widget.Toast.LENGTH_SHORT)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return postulados != null ? postulados.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCalificacion;
        android.widget.ImageView ivGuardar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCalificacion = itemView.findViewById(R.id.tvCalificacion);
            ivGuardar = itemView.findViewById(R.id.ivGuardar);
        }
    }
}
