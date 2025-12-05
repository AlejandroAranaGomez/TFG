package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;

import java.util.ArrayList;
import java.util.List;

public class AlimentosAdapter extends RecyclerView.Adapter<AlimentosAdapter.FoodViewHolder> {

    private List<ApiAlimentosDTO> lista = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ApiAlimentosDTO alimento);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<ApiAlimentosDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.propiedad_alimentos, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentosAdapter.FoodViewHolder holder, int position) {
        ApiAlimentosDTO alimento =  lista.get(position);
        holder.txtNombre.setText(alimento.getNombre());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(alimento);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.nombreAlimento);
        }
    }
}
