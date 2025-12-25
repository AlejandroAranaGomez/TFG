package com.trabajo.fitnessapp.presentacion.adaptador;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DiasAdapter extends RecyclerView.Adapter<DiasAdapter.DiaViewHolder> {

    private List<DiaEnDietaDTO> listaDias = new ArrayList<>();
    private OnDiaClickListener listener;
    private int posicionActual;

    public interface OnDiaClickListener {
        void onDiaClick(DiaEnDietaDTO dia);
        void onAddDiaClick();
    }

    public void setOnDiaClickListener(DiasAdapter.OnDiaClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<DiaEnDietaDTO> nuevaLista) {
        this.listaDias = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Si solo hay un item es el boton de añadir
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anhadir_dia, parent, false);
            return new DiaViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dias_dieta, parent, false);
        return new DiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (position < listaDias.size()) {

            DiaEnDietaDTO dia = listaDias.get(position);


            String diaSemana = dia.getDiaDeLaSemana().name();
            holder.nombre.setText(diaSemana.substring(0, 3).toUpperCase(Locale.getDefault()));

            holder.calorias.setText(String.format(Locale.getDefault(), "%.2f", dia.getCaloriasTotales()));

            if (position == posicionActual) {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.BotonesImagenes, context.getTheme()));
                holder.nombre.setTextColor(Color.BLACK);
                holder.calorias.setTextColor(Color.BLACK);
            } else {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.fondoDias, context.getTheme()));
                holder.nombre.setTextColor(Color.WHITE);
                holder.calorias.setTextColor(Color.WHITE);
            }

            holder.itemView.setOnClickListener(v -> {
                int currentPosition = holder.getBindingAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < listaDias.size()) {
                    int previousSelected = posicionActual;
                    posicionActual = currentPosition;

                    if (previousSelected != currentPosition) {
                        notifyItemChanged(previousSelected);
                        notifyItemChanged(posicionActual);
                    }
                    if (listener != null) listener.onDiaClick(listaDias.get(currentPosition));
                }
            });

        } else {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAddDiaClick();
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaDias.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == listaDias.size()) ? 1 : 0;
    }

    public void setPosicionActual(int posicion) {
        posicionActual = posicion;
        notifyDataSetChanged();
    }

    static class DiaViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        TextView calorias;
        LinearLayout layout;

        public DiaViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.diaNombre);
            calorias = itemView.findViewById(R.id.diaCalorias);
            layout = itemView.findViewById(R.id.layoutDia);

        }
    }
}
