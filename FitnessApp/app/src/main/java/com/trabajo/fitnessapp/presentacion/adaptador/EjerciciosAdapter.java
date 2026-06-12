package com.trabajo.fitnessapp.presentacion.adaptador;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;

import java.util.ArrayList;
import java.util.List;

public class EjerciciosAdapter extends RecyclerView.Adapter<EjerciciosAdapter.EjercicioViewHolder> {

    private List<ApiEjercicioDTO> listaEjercicios = new ArrayList<>();
    private boolean mostrarBotonAnhadir = false;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ApiEjercicioDTO ejercicioDTO);
        void onAñadirEjercicio(ApiEjercicioDTO ejercicioDTO);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<ApiEjercicioDTO> lista) {
        this.listaEjercicios = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setMostrarBotonAñadir(boolean mostrar) {
        this.mostrarBotonAnhadir = mostrar;
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.datos_ejercicios, parent, false);
        return new EjercicioViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {

        if (mostrarBotonAnhadir) {
            holder.botonAnhadirEjercicio.setVisibility(View.VISIBLE);
        } else {
            holder.botonAnhadirEjercicio.setVisibility(View.GONE);
        }

        ApiEjercicioDTO ejercicio = listaEjercicios.get(position);
        holder.nombre.setText(ejercicio.getName());

        if (ejercicio.getImages() != null && !ejercicio.getImages().isEmpty()) {
            Log.d("IMG", ejercicio.getImages().get(0));
            Glide.with(holder.itemView.getContext())
                    .load(ejercicio.getImages().get(0))
                    .into(holder.gifEjercicio);
        }

        holder.botonAnhadirEjercicio.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAñadirEjercicio(ejercicio);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null)  {
                listener.onItemClick(ejercicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEjercicios.size();
    }

    static class EjercicioViewHolder extends RecyclerView.ViewHolder {

        TextView nombre;
        ImageView gifEjercicio;
        Button botonAnhadirEjercicio;
        public EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreEjercicio);
            gifEjercicio = itemView.findViewById(R.id.imagenEjercicio);
            botonAnhadirEjercicio = itemView.findViewById(R.id.botonAnhadirEjercicio);
        }

    }
}
