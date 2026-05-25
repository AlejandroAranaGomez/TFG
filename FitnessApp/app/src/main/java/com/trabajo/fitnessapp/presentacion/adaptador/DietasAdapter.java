package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DietasAdapter extends RecyclerView.Adapter<DietasAdapter.DietaViewHolder> {

    private List<DietaCompletaDTO> listaDietas = new ArrayList<>();
    private OnDietaClickListener listener;

    public interface OnDietaClickListener {
        void onMostrarPlanClick(DietaCompletaDTO dieta);
        void onEditarClick(DietaCompletaDTO dieta);
        void onBorrarClick(DietaCompletaDTO dieta);
    }
    public void setOnDietaClickListener(OnDietaClickListener listener) {
        this.listener = listener;
    }
    public void setLista(List<DietaCompletaDTO> nuevaLista) {
        this.listaDietas = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DietaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostrar_dietas, parent, false);
        return new DietaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DietaViewHolder holder, int position) {
        DietaCompletaDTO dieta = listaDietas.get(position);

        holder.textoNombre.setText(dieta.getNombre());
        holder.textoCalorias.setText(String.format(Locale.getDefault(), "%.2f kcal", dieta.getCaloriasTotales()));

        holder.textoProteinas.setText(String.format(Locale.getDefault(), "Prot: %.2fg", dieta.getProteinas()));
        holder.textoCarbohidratos.setText(String.format(Locale.getDefault(), "Carb: %.2fg", dieta.getCarbohidratos()));
        holder.textoGrasas.setText(String.format(Locale.getDefault(), "Grasas: %.2fg", dieta.getGrasas()));

        holder.textoNombreAtras.setText(dieta.getNombre());
        holder.descripcion.setText(dieta.getDescripcion());


        if (dieta.isActiva()) {
            holder.textoEstado.setText("ACTIVA");
            holder.textoEstado.setTextColor(holder.itemView.getContext().getColor(R.color.activa));
            holder.textoEstado.setVisibility(View.VISIBLE);
        } else {
            holder.textoEstado.setText("NO ACTIVA");
            holder.textoEstado.setTextColor(holder.itemView.getContext().getColor(R.color.borrar));
            holder.textoEstado.setVisibility(View.VISIBLE);
        }

        holder.botonVerPlan.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMostrarPlanClick(dieta);
            }
        });

        holder.botonEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(dieta);
            }
        });

        holder.botonBorrar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBorrarClick(dieta);
            }
        });

        holder.layoutFrente.setVisibility(View.VISIBLE);
        holder.layoutFrente.setRotationY(0);
        holder.layoutFrente.setAlpha(1f);

        holder.layoutAtras.setVisibility(View.GONE);
        holder.layoutAtras.setRotationY(0);


        View.OnClickListener girarListener = v -> girarTarjeta(holder.layoutFrente, holder.layoutAtras);

        holder.layoutFrente.setOnClickListener(girarListener);
        holder.layoutAtras.setOnClickListener(girarListener);

    }


    @Override
    public int getItemCount() {
        return listaDietas.size();
    }

    static class DietaViewHolder extends RecyclerView.ViewHolder {
        TextView textoNombre, textoCalorias, textoProteinas, textoCarbohidratos, textoGrasas, textoEstado, textoNombreAtras, descripcion;

        View layoutFrente, layoutAtras, contenedor;
        Button botonEditar, botonBorrar, botonVerPlan;

        public DietaViewHolder(@NonNull View itemView) {
            super(itemView);
            textoNombre = itemView.findViewById(R.id.nombreDieta);
            textoCalorias = itemView.findViewById(R.id.caloriasDieta);
            textoProteinas = itemView.findViewById(R.id.proteinas);
            textoCarbohidratos = itemView.findViewById(R.id.carbohidratos);
            textoGrasas = itemView.findViewById(R.id.grasas);
            botonEditar = itemView.findViewById(R.id.botonEditarDieta);
            botonBorrar = itemView.findViewById(R.id.botonBorrarDieta);
            botonVerPlan = itemView.findViewById(R.id.botonVerPlanDieta);
            textoEstado = itemView.findViewById(R.id.textoEstado);

            layoutFrente = itemView.findViewById(R.id.layoutFrente);
            layoutAtras = itemView.findViewById(R.id.layoutAtras);
            contenedor = itemView.findViewById(R.id.contenedorGiratorio);

            textoNombreAtras = itemView.findViewById(R.id.nombreDietaAtras);
            descripcion = itemView.findViewById(R.id.textoDescripcionCompleta);

            // Para el giro
            float scale = itemView.getResources().getDisplayMetrics().density;
            layoutFrente.setCameraDistance(8000 * scale);
            layoutAtras.setCameraDistance(8000 * scale);
        }
    }

    private void girarTarjeta(View frente, View atras) {

        final View visible;
        final View invisible;

        if (frente.getVisibility() == View.VISIBLE) {
            visible = frente;
            invisible = atras;
        } else {
            visible = atras;
            invisible = frente;
        }

        visible.animate().rotationY(90f).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                visible.setVisibility(View.GONE);

                invisible.setVisibility(View.VISIBLE);
                invisible.setRotationY(-90f);

                invisible.animate().rotationY(0f).setDuration(300).setListener(null);
            }
        });
    }
}
