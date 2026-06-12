package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;
import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;

import java.util.ArrayList;
import java.util.List;

public class EjerciciosRutinaAdapter
        extends RecyclerView.Adapter<EjerciciosRutinaAdapter.ViewHolder> {

    public static final int EJERCICIO = 0;
    public static final int BOTON_ANHADIR = 1;
    private List<EjercicioDTO> ejercicios = new ArrayList<>();
    private List<ApiEjercicioDTO> apiEjercicios = new ArrayList<>();

    public interface OnEjercicioClickListener {
        void onBorrar(EjercicioDTO ejercicio);
        void onAnhadirEjercicio();
        void onClick(EjercicioDTO ejercicio);
    }

    private OnEjercicioClickListener listener;

    public void setOnEjercicioClickListener(OnEjercicioClickListener listener) {
        this.listener = listener;
    }

    public void setEjercicios(List<EjercicioDTO> ejercicios) {
        this.ejercicios = ejercicios != null ? ejercicios : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setApiEjercicios(List<ApiEjercicioDTO> lista) {
        this.apiEjercicios = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == BOTON_ANHADIR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anhadir_ejercicio, parent, false);
            return new EjerciciosRutinaAdapter.ViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.datos_ejercicios, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < ejercicios.size()) {

            EjercicioDTO ejercicio = ejercicios.get(position);
            holder.nombre.setText(ejercicio.getNombre());
            holder.botonBorrar.setVisibility(View.VISIBLE);

            ApiEjercicioDTO apiEjercicio = null;
            if (ejercicio.getIdApi() != null) {
                for (ApiEjercicioDTO a : apiEjercicios) {
                    if (a.getId().equals(ejercicio.getIdApi())) {
                        apiEjercicio = a;
                        break;
                    }
                }
            }

            if (apiEjercicio != null && apiEjercicio.getImages() != null && !apiEjercicio.getImages().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(apiEjercicio.getImages().get(0))
                        .into(holder.imagenEjercicio);
            } else {
                // Imagen por defecto si no hay
                holder.imagenEjercicio.setImageResource(R.drawable.ic_launcher_foreground);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClick(ejercicio);
                }
            });

            holder.botonBorrar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBorrar(ejercicio);
                }
            });

        } else {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAnhadirEjercicio();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ejercicios.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == ejercicios.size())
                ? BOTON_ANHADIR
                : EJERCICIO;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre;
        ImageButton botonBorrar;
        ImageView imagenEjercicio;

        ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreEjercicio);
            botonBorrar = itemView.findViewById(R.id.botonBorrarEjercicio);
            imagenEjercicio = itemView.findViewById(R.id.imagenEjercicio);
        }
    }
}

