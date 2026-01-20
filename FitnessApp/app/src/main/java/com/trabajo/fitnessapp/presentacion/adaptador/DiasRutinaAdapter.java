package com.trabajo.fitnessapp.presentacion.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DiasRutinaAdapter extends RecyclerView.Adapter<DiasRutinaAdapter.DiaViewHolder> {

    private List<DiaEnRutinaDTO> listaDias = new ArrayList<>();
    private OnDiaClickListener listener;
    private int posicionActual;

    public interface OnDiaClickListener {
        void onDiaClick(DiaEnRutinaDTO dia);
        void onAddDiaClick();
    }

    public void setOnDiaClickListener(DiasRutinaAdapter.OnDiaClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<DiaEnRutinaDTO> nuevaLista) {
        this.listaDias = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Si solo hay un item es el boton de añadir
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anhadir_dia, parent, false);
            return new DiasRutinaAdapter.DiaViewHolder(view);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dias_rutina, parent, false);
        return new DiasRutinaAdapter.DiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if (position < listaDias.size()) {

            DiaEnRutinaDTO dia = listaDias.get(position);


            String diaSemana = dia.getDiaDeLaSemana().name();
            holder.nombre.setText(diaSemana.substring(0, 3).toUpperCase(Locale.getDefault()));

            if (position == posicionActual) {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.BotonesImagenesRutinas, context.getTheme()));
                holder.nombre.setTextColor(Color.BLACK);
            } else {
                holder.layout.setBackgroundColor(context.getResources().getColor(R.color.fondoContenedorRutinas, context.getTheme()));
                holder.nombre.setTextColor(Color.WHITE);
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
            MaterialCardView card = holder.itemView.findViewById(R.id.anhadirDia);
            card.setCardBackgroundColor(
                    holder.itemView.getContext()
                            .getResources()
                            .getColor(R.color.fondoContenedorRutinas, holder.itemView.getContext().getTheme())
            );
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
        LinearLayout layout;


        public DiaViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.diaNombreRutina);
            layout = itemView.findViewById(R.id.layoutDiaRutina);
        }
    }

}
