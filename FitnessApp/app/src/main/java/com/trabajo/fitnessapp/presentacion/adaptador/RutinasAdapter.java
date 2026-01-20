package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;

import java.util.ArrayList;
import java.util.List;

public class RutinasAdapter extends RecyclerView.Adapter<RutinasAdapter.RutinaViewHolder> {
    private List<RutinaCompletaDTO> listaRutinas = new ArrayList<>();
    private OnRutinaClickListener listener;

    public interface OnRutinaClickListener {
        void onVerDiasClick(RutinaCompletaDTO rutina);
        void onEditarClick(RutinaCompletaDTO rutina);
        void onBorrarClick(RutinaCompletaDTO rutina);
    }

    public void setOnRutinaClickListener(OnRutinaClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<RutinaCompletaDTO> nuevaLista) {
        this.listaRutinas = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostrar_rutinas, parent, false);
        return new RutinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        RutinaCompletaDTO rutina = listaRutinas.get(position);

        holder.textoNombre.setText(rutina.getNombreRutinaCompleta());
        holder.textoResumen.setText(rutina.getResumen());

        holder.botonVerDias.setOnClickListener(v -> {
            if (listener != null) {
                listener.onVerDiasClick(rutina);
            }
        });

        holder.botonEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(rutina);
            }
        });

        holder.botonBorrar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBorrarClick(rutina);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRutinas.size();
    }

    static class RutinaViewHolder extends RecyclerView.ViewHolder {
        TextView textoNombre, textoResumen;

        Button botonEditar, botonBorrar, botonVerDias;

        public RutinaViewHolder(@NonNull View itemview) {
            super(itemview);

            textoNombre = itemview.findViewById(R.id.nombreRutina);
            textoResumen = itemview.findViewById(R.id.descripcionRutina);

            botonEditar = itemview.findViewById(R.id.botonEditarRutina);
            botonBorrar = itemview.findViewById(R.id.botonBorrarRutina);
            botonVerDias = itemview.findViewById(R.id.botonVerDiasRutina);
        }
    }
}
