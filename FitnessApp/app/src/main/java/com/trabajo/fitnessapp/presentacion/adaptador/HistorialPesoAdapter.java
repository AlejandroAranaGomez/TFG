package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.HistorialPesoDTO;

import java.util.List;

public class HistorialPesoAdapter extends RecyclerView.Adapter<HistorialPesoAdapter.ViewHolder> {

    private List<HistorialPesoDTO> lista;

    public HistorialPesoAdapter(List<HistorialPesoDTO> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_peso, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistorialPesoDTO item = lista.get(position);

        holder.textPeso.setText(item.getPeso() + " kg");
        holder.textFecha.setText(item.getFecha());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textPeso;
        TextView textFecha;

        public ViewHolder(View itemView) {
            super(itemView);

            textPeso = itemView.findViewById(R.id.textPeso);
            textFecha = itemView.findViewById(R.id.textFecha);
        }
    }

}
