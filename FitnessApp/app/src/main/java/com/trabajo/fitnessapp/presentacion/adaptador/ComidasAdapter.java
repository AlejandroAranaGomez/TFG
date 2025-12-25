package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ComidaDTO;

import java.util.ArrayList;
import java.util.List;

public class ComidasAdapter extends RecyclerView.Adapter<ComidasAdapter.ComidaViewHolder> {

    private List<ComidaDTO> comidas = new ArrayList<>();
    private OnComidaClickListener listener;

    public interface OnComidaClickListener {
        void onEditarComida(ComidaDTO comida);
        void onBorrarComida(ComidaDTO comida);
        void onAddComidaClick();
        void onVerIngredientes(ComidaDTO comida);
    }

    public void setOnComidaClickListener(ComidasAdapter.OnComidaClickListener listener) {
        this.listener = listener;
    }


    public void setLista(List<ComidaDTO> nuevaLista) {
        this.comidas = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Si solo hay un item es el boton de añadir
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anhadir_comida, parent, false);
            return new ComidasAdapter.ComidaViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mostrar_comida, parent, false);
        return new ComidaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {

        if (position < comidas.size()) {
            ComidaDTO comida = comidas.get(position);
            holder.nombre.setText(comida.getNombre());
            holder.calorias.setText(comida.getCaloriasTotales() + " kcal");
            holder.proteinas.setText("P: " + comida.getProteinas() + "g");
            holder.carbohidratos.setText("C: " + comida.getCarbohidratos() + "g");
            holder.grasas.setText("G: " + comida.getGrasas() + "g");

            holder.botonEditar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditarComida(comida);
                }
            });

            holder.botonBorrar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onBorrarComida(comida);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onVerIngredientes(comida);
                }
            });

        } else {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddComidaClick();
                }
            });
                }
            }

    @Override
    public int getItemCount() {
        return comidas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == comidas.size()) ? 1 : 0;
    }

    static class ComidaViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, calorias, proteinas, carbohidratos, grasas;
        ImageButton botonBorrar, botonEditar;
        public ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textoNombre);
            calorias = itemView.findViewById(R.id.textoCalorias);
            proteinas = itemView.findViewById(R.id.textoProteinas);
            carbohidratos = itemView.findViewById(R.id.textoCarbohidratos);
            grasas = itemView.findViewById(R.id.textoGrasas);
            botonBorrar = itemView.findViewById(R.id.botonBorrarComida);
            botonEditar = itemView.findViewById(R.id.botonEditarComida);
        }



    }
}
