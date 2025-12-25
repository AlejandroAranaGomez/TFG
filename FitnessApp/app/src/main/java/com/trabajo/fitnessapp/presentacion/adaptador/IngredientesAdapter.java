package com.trabajo.fitnessapp.presentacion.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;
import com.trabajo.fitnessapp.presentacion.Utils.ModoIngrediente;

import java.util.ArrayList;
import java.util.List;

public class IngredientesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SOLO_LECTURA = 0;
    private static final int EDICION = 1;

    ModoIngrediente modo = ModoIngrediente.SOLO_LECTURA;
    private List<IngredienteDTO> lista = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == EDICION) {
            View view = inflater.inflate(R.layout.mostrar_ingredientes, parent, false);
            return new IngredienteEdicionViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.detalles_ingrediente, parent, false);
            return new IngredienteLecturaViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        IngredienteDTO ingrediente = lista.get(position);

        if (holder instanceof IngredienteLecturaViewHolder) {
            IngredienteLecturaViewHolder ingredienteLectura = (IngredienteLecturaViewHolder) holder;
            ingredienteLectura.nombre.setText(ingrediente.getNombre());
            ingredienteLectura.cantidadEnGramos.setText(ingrediente.getCantidadEnGramos() + " g");
            ingredienteLectura.propiedades.setText(
                    "Kcal: " + ingrediente.getCaloriasTotales() +
                            " | P: " + ingrediente.getProteinas() +
                            " | C: " + ingrediente.getCarbohidratos() +
                            " | G: " + ingrediente.getGrasas()
            );

            ingredienteLectura.itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(ingrediente);
            });

        } else if (holder instanceof IngredienteEdicionViewHolder) {
            IngredienteEdicionViewHolder ingredienteEdicionViewHolder = (IngredienteEdicionViewHolder) holder;
            ingredienteEdicionViewHolder.nombre.setText(ingrediente.getNombre());
            ingredienteEdicionViewHolder.cantidad.setText("Cantidad: " + ingrediente.getCantidadEnGramos() + " g");
            ingredienteEdicionViewHolder.calorias.setText(ingrediente.getCaloriasTotales() + " kcal");
            ingredienteEdicionViewHolder.proteinas.setText("P: " + ingrediente.getProteinas());
            ingredienteEdicionViewHolder.carbohidratos.setText("C: " + ingrediente.getCarbohidratos());
            ingredienteEdicionViewHolder.grasas.setText("G: " + ingrediente.getGrasas());

            ingredienteEdicionViewHolder.botonEditar.setOnClickListener(v -> {
                if (listener != null) listener.onEditarClick(ingrediente);
            });

            ingredienteEdicionViewHolder.botonBorrar.setOnClickListener(v -> {
                if (listener != null) listener.onBorrarClick(ingrediente);
            });
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public interface OnItemClickListener {
        void onItemClick(IngredienteDTO ingrediente);
        void onEditarClick(IngredienteDTO ingrediente);
        void onBorrarClick(IngredienteDTO ingrediente);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<IngredienteDTO> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public void setModo(ModoIngrediente modo) {
        this.modo = modo;
    }

    @Override
    public int getItemViewType(int position) {
        return modo == ModoIngrediente.EDICION ? EDICION : SOLO_LECTURA;
    }


    static class IngredienteLecturaViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, cantidadEnGramos, propiedades;
        public IngredienteLecturaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreIngrediente);
            cantidadEnGramos = itemView.findViewById(R.id.cantidadIngrediente);
            propiedades = itemView.findViewById(R.id.propiedadesIngrediente);
        }
    }

    static class IngredienteEdicionViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, cantidad, calorias, proteinas, carbohidratos, grasas;
        ImageButton botonEditar, botonBorrar;

        public IngredienteEdicionViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.textoNombre);
            cantidad = itemView.findViewById(R.id.textoCantidad);
            calorias = itemView.findViewById(R.id.textoCalorias);
            proteinas = itemView.findViewById(R.id.textoProteinas);
            carbohidratos = itemView.findViewById(R.id.textoCarbohidratos);
            grasas = itemView.findViewById(R.id.textoGrasas);
            botonEditar = itemView.findViewById(R.id.botonEditarIngrediente);
            botonBorrar = itemView.findViewById(R.id.botonBorrarIngrediente);
        }
    }
}
