package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.SerieDTO;

import java.util.ArrayList;
import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.ViewHolder> {
    public interface OnSerieClickListener {
        void onEditar(SerieDTO serieDTO);
        void onBorrar(SerieDTO serieDTO);
    }

    private List<SerieDTO> lista = new ArrayList<>();
    private OnSerieClickListener listener;

    public void setListener(OnSerieClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<SerieDTO> lista) {
        this.lista = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.serie_ejercicio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SerieDTO serieDTO = lista.get(position);

        holder.textoNumeroSerie.setText(String.valueOf(position + 1));

        if (serieDTO.getSerieAnterior() != null && !serieDTO.getSerieAnterior().isEmpty()) {
            holder.textoSerieAnterior.setText(serieDTO.getSerieAnterior());
        } else {
            holder.textoSerieAnterior.setText("--------------------");
        }

        holder.textoPeso.setText(String.valueOf(serieDTO.getPeso()));
        holder.textoReps.setText(String.valueOf(serieDTO.getRepeticiones()));


        holder.botonEditar.setOnClickListener(v -> {
            try {
                float nuevoPeso = Float.parseFloat(
                        holder.textoPeso.getText().toString()
                );
                int nuevasReps = Integer.parseInt(
                        holder.textoReps.getText().toString()
                );

                serieDTO.setPeso(nuevoPeso);
                serieDTO.setRepeticiones(nuevasReps);

                if (listener != null) {
                    listener.onEditar(serieDTO);
                }

            } catch (NumberFormatException e) {
                Toast.makeText(holder.itemView.getContext(), "Valores inválidos", Toast.LENGTH_SHORT).show();
            }
        });

        holder.botonBorrar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBorrar(serieDTO);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textoNumeroSerie;
        TextView textoSerieAnterior;
        TextView textoPeso;
        TextView textoReps;
        ImageButton botonEditar;
        ImageButton botonBorrar;

        ViewHolder(View itemView) {
            super(itemView);
            textoNumeroSerie = itemView.findViewById(R.id.textoNumeroSerie);
            textoSerieAnterior = itemView.findViewById(R.id.textoSerieAnterior);
            textoPeso = itemView.findViewById(R.id.editPeso);
            textoReps = itemView.findViewById(R.id.editReps);
            botonEditar = itemView.findViewById(R.id.botonEditar);
            botonBorrar = itemView.findViewById(R.id.botonBorrar);
        }
    }


}