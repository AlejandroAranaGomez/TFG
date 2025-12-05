package com.trabajo.fitnessapp.presentacion.adaptador;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;

import java.util.ArrayList;
import java.util.List;

public class AlimentosPersonalizadosAdapter extends RecyclerView.Adapter<AlimentosPersonalizadosAdapter.FoodViewHolder> {

    private List<AlimentoDTO> lista = new ArrayList<>();
    private AlimentosPersonalizadosAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AlimentoDTO alimento);
        void onEditarClick(AlimentoDTO alimento);
        void onBorrarClick(AlimentoDTO alimento);
    }

    public void setOnItemClickListener (AlimentosPersonalizadosAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<AlimentoDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.propiedad_alimentos_propios, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentosPersonalizadosAdapter.FoodViewHolder holder, int position) {
        AlimentoDTO alimento =  lista.get(position);
        holder.txtNombre.setText(alimento.getNombre());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(alimento);
            }
        });

        holder.botonEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(alimento);
            }
        });

        holder.botonBorrar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBorrarClick(alimento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;

        ImageButton botonEditar, botonBorrar;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.nombreAlimento);
            botonBorrar = itemView.findViewById(R.id.botonBorrar);
            botonEditar = itemView.findViewById(R.id.botonEditar);
        }
    }

}
