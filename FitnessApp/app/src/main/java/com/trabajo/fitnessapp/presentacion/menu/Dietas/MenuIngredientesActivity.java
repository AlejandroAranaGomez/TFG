package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;
import com.trabajo.fitnessapp.datos.Utils.ModoIngrediente;
import com.trabajo.fitnessapp.presentacion.adaptador.IngredientesAdapter;

import java.util.ArrayList;

public class MenuIngredientesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIngredientes;
    private IngredientesAdapter ingredientesAdapter;
    private ImageButton botonVolverAtras;
    private IngredientesViewModel ingredientesViewModel;
    private Long idComida;
    private Long idUsuario;
    private Button botonAnhadirIngrediente;
    private IngredienteDTO ingredienteActual;
    private AlertDialog dialogoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingredientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idComida = getIntent().getLongExtra("ID_COMIDA", -1L);
        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1L);


        enlazarVistas();

        recyclerViewIngredientes.setLayoutManager(new LinearLayoutManager(this));
        ingredientesAdapter = new IngredientesAdapter();
        ingredientesAdapter.setModo(ModoIngrediente.EDICION);
        recyclerViewIngredientes.setAdapter(ingredientesAdapter);

        ingredientesAdapter.setOnItemClickListener(new IngredientesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(IngredienteDTO ingrediente) {

            }

            @Override
            public void onEditarClick(IngredienteDTO ingrediente) {
                mostrarEditarIngrediente(ingrediente);
            }
            @Override
            public void onBorrarClick(IngredienteDTO ingrediente) {
                mostrarPopUpBorrarIngrediente(ingrediente);
            }
        });

        ingredientesViewModel = new ViewModelProvider(this).get(IngredientesViewModel.class);

        cargarIngredientes();
        configurarBotones();
        observarIngredientes();
    }

    private void enlazarVistas() {
        recyclerViewIngredientes = findViewById(R.id.recyclerIngredientes);
        botonAnhadirIngrediente = findViewById(R.id.botonAnhadirIngrediente);
        botonVolverAtras = findViewById(R.id.botonVolverActividad);
    }

    private void configurarBotones() {
        botonAnhadirIngrediente.setOnClickListener(v -> {

            Intent intent = new Intent(MenuIngredientesActivity.this, AnhadirIngredienteActivity.class);
            intent.putExtra("ID_COMIDA", idComida);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
        });

        botonVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarIngredientes() {
        ingredientesViewModel.obtenerIngredientes(idComida);
    }

    private void mostrarPopUpBorrarIngrediente(IngredienteDTO ingrediente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);

        titulo.setText("Borrar Ingrediente");
        pregunta.setText("¿Deseas borrar " + ingrediente.getNombre() + "?");

        view.findViewById(R.id.botonCancelarBorrar)
                .setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.botonConfirmarBorrar)
                .setOnClickListener(v -> {
                    borrarIngredienteConfirmado(ingrediente);
                    dialog.dismiss();
                });

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void borrarIngredienteConfirmado(IngredienteDTO ingrediente) {
        ingredientesViewModel.borrarIngrediente(idComida, ingrediente.getIdIngrediente());
    }

    private void mostrarEditarIngrediente(IngredienteDTO ingrediente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.editar_dia, null);
        builder.setView(view);

        TextView titulo = view.findViewById(R.id.tituloEditarDia);
        TextView nombreEtiqueta = view.findViewById(R.id.textoNombreEditarDia);
        EditText editCantidad = view.findViewById(R.id.editNombre);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);

        nombreEtiqueta.setText("Cantidad en Gramos");

        titulo.setText("Editar cantidad");
        editCantidad.setInputType(
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        editCantidad.setText(
                String.valueOf(ingrediente.getCantidadEnGramos())
        );

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevaCantidad = editCantidad.getText().toString().trim();

                float nuevaCantidadNumero;

                try {
                    nuevaCantidadNumero = Float.parseFloat(nuevaCantidad);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Debes escribir una cantidad", Toast.LENGTH_SHORT).show();
                    return;
                }

                IngredienteDTO ingredienteEditado = new IngredienteDTO();
                ingredienteEditado.setIdIngrediente(ingrediente.getIdIngrediente());
                ingredienteEditado.setNombre(ingrediente.getNombre());
                ingredienteEditado.setCantidadEnGramos(nuevaCantidadNumero);
                ingredienteEditado.setCaloriasTotales(ingrediente.getCaloriasTotales());
                ingredienteEditado.setProteinas(ingrediente.getProteinas());
                ingredienteEditado.setCarbohidratos(ingrediente.getCarbohidratos());
                ingredienteEditado.setGrasas(ingrediente.getGrasas());

                actualizarIngredienteConfirmada(ingredienteEditado);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Error al editar", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    private void actualizarIngredienteConfirmada(IngredienteDTO ingredienteEditado) {
        ingredientesViewModel.editarIngrediente(ingredienteEditado.getIdIngrediente(), idComida, ingredienteEditado);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idComida != null && idComida != -1L) {
            ingredientesViewModel.obtenerIngredientes(idComida);
        }
    }

    private void observarIngredientes() {
        ingredientesViewModel.getIngredientes().observe(this, lista -> {
            ingredientesAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        ingredientesViewModel.getIngredienteActualizadoExito().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Ingrediente actualizado correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        ingredientesViewModel.getIngredienteBorradoExito().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Ingrediente eliminado correctamente", Toast.LENGTH_SHORT).show();
                cargarIngredientes();
            }
        });
    }
}