package com.trabajo.fitnessapp.presentacion.adaptador;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ComidaSeguimientoDTO;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;

import java.util.ArrayList;
import java.util.List;

public class ComidasHoyAdapter extends RecyclerView.Adapter<ComidasHoyAdapter.ViewHolder> {

    private List<ComidaSeguimientoDTO> comidasHoy = new ArrayList<>();
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onCheckClick(ComidaSeguimientoDTO comidaSeguimientoDTO, boolean marcada);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<ComidaSeguimientoDTO> nuevaLista) {
        this.comidasHoy = nuevaLista;
        notifyDataSetChanged();
    }

    public List<ComidaSeguimientoDTO> getLista() {
        return comidasHoy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comida_hoy, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComidaSeguimientoDTO comida = comidasHoy.get(position);

        holder.nombre.setText(comida.getNombre());
        holder.propiedades.setText(comida.getCaloriasTotales() + " kcal" + " - " +
                                    "P: " + comida.getProteinas() + " g" + " - " +
                                    "C: " + comida.getCarbohidratos() + " g" + " - " +
                                    "G: " + comida.getGrasas() + " g");

        if (comida.getIngredientes() != null && !comida.getIngredientes().isEmpty()) {
            StringBuilder ingredientesTexto = new StringBuilder();

            for (int i = 0; i < comida.getIngredientes().size(); i++) {
                IngredienteDTO ingrediente = comida.getIngredientes().get(i);
                ingredientesTexto.append(ingrediente.getNombre());

                if (i < comida.getIngredientes().size() - 1) {
                    ingredientesTexto.append(", ");
                }
            }

            holder.ingredientes.setText(ingredientesTexto.toString());
        } else {
            holder.ingredientes.setText("Sin ingredientes");
        }

        holder.checkComida.setOnCheckedChangeListener(null);
        holder.checkComida.setChecked(comida.isRegistrada());

        actualizarColorBorde(holder, comida.isRegistrada());

        holder.checkComida.setOnCheckedChangeListener((buttonView, isChecked) -> {

            comida.setRegistrada(isChecked);
            actualizarColorBorde(holder, isChecked);

            if (listener != null) {
                listener.onCheckClick(comida, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comidasHoy.size();
    }

    private void actualizarColorBorde(ViewHolder holder, boolean isChecked) {
        GradientDrawable drawable;

        if (holder.linearLayout.getBackground() instanceof GradientDrawable) {
            drawable = (GradientDrawable) holder.linearLayout.getBackground();
        } else {
            drawable = new GradientDrawable();
            drawable.setCornerRadius(16f);
            drawable.setColor(holder.itemView.getContext().getResources().getColor(R.color.fondoBarraMenu));
        }

        int colorBorde = isChecked
                ? holder.itemView.getContext().getResources().getColor(R.color.BotonesImagenesDietas)
                : holder.itemView.getContext().getResources().getColor(R.color.fondoBarraMenu);

        drawable.setStroke(6, colorBorde);

        // Aplicar el drawable al layout
        holder.linearLayout.setBackground(drawable);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, ingredientes, propiedades;
        CheckBox checkComida;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombreComida);
            ingredientes = itemView.findViewById(R.id.txtIngredientes);
            propiedades = itemView.findViewById(R.id.txtPropiedades);
            checkComida = itemView.findViewById(R.id.checkComida);
            linearLayout = itemView.findViewById(R.id.contenedorComida);
        }
    }

}
