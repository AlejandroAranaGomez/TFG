package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;
import com.trabajo.fitnessapp.presentacion.adaptador.AlimentosAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.AlimentosPersonalizadosAdapter;

import java.util.ArrayList;

public class MenuAlimentosActivity extends AppCompatActivity {
    private AlimentosViewModel alimentosViewModel;
    private Button botonBuscar;
    private Button botondespliegaAlimentos;
    private ImageButton botonVolverActividad;
    private ImageButton botonCrearAlimento;
    private boolean desplegado = false;
    private EditText buscadorAlimentos;
    private AlimentosAdapter alimentosAdapter;
    private AlimentosPersonalizadosAdapter misAlimentosAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerAlimentosPropios;
    private Long idUsuario;
    private AlertDialog dialogoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_alimentos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1l);

        enlazarVistas();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alimentosAdapter = new AlimentosAdapter();
        recyclerView.setAdapter(alimentosAdapter);
        recyclerAlimentosPropios.setLayoutManager(new LinearLayoutManager(this));
        misAlimentosAdapter = new AlimentosPersonalizadosAdapter();
        recyclerAlimentosPropios.setAdapter(misAlimentosAdapter);

        alimentosAdapter.setOnItemClickListener(alimento -> mostrarPopup(alimento));

        misAlimentosAdapter.setOnItemClickListener(new AlimentosPersonalizadosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlimentoDTO alimento) {
                mostrarPopupPropios(alimento);
            }

            @Override
            public void onEditarClick(AlimentoDTO alimento) {
                mostrarPopupActualizar(alimento);
            }

            @Override
            public void onBorrarClick(AlimentoDTO alimento) { mostrarPopUpBorrar(alimento); }
        });

        botonCrearAlimento.setOnClickListener(v -> mostrarPopUpCrearAlimento());

        alimentosViewModel = new ViewModelProvider(this).get(AlimentosViewModel.class);

        cargarDatos();
        observarAlimentos();

        configurarBotones();
    }

    private void enlazarVistas() {
        recyclerView = findViewById(R.id.recyclerAlimentos);
        botonBuscar = findViewById(R.id.botonBuscarAlimento);
        botonVolverActividad = findViewById(R.id.botonVolverActividad);
        buscadorAlimentos = findViewById(R.id.buscadorAlimentos);
        botondespliegaAlimentos = findViewById(R.id.botonDesplegarMisAlimentos);
        recyclerAlimentosPropios = findViewById(R.id.recyclerMisAlimentos);
        botonCrearAlimento = findViewById(R.id.botonCrearAlimento);
    }

    private void configurarBotones() {
        botonBuscar.setOnClickListener(v -> {
            String query = buscadorAlimentos.getText().toString();
            if (!query.isEmpty()) {
                buscarEnApi(query);
            } else {
                Toast.makeText(this, "Debes escribir un alimento para poder buscar.", Toast.LENGTH_SHORT).show();
            }
        });

        botondespliegaAlimentos.setOnClickListener(v -> {
            if (desplegado) {
                recyclerAlimentosPropios.setVisibility(View.GONE);
                botondespliegaAlimentos.setText("▼ Mis Alimentos Personalizados");
                desplegado = false;
            } else {
                recyclerAlimentosPropios.setVisibility(View.VISIBLE);
                botondespliegaAlimentos.setText("▲ Mis Alimentos Personalizados");
                desplegado = true;
            }
        });

        botonVolverActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void buscarEnApi(String query) {
        alimentosViewModel.buscarAlimentosGlobales(query).observe(this, lista -> {
            if (lista != null && !lista.isEmpty()) {
                alimentosAdapter.setLista(lista);
            } else {
                Toast.makeText(this, "No se encontro el alimento que estas intentando buscar.", Toast.LENGTH_SHORT).show();
                alimentosAdapter.setLista(new ArrayList<>());
            }
        });
    }

    private void mostrarPopup(ApiAlimentosDTO alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.detalles_comida, null);
        builder.setView(view);

        TextView tituloPopUp = view.findViewById(R.id.tituloPopUp);
        TextView detallesPopUp = view.findViewById(R.id.detallesPopUp);
        Button botonPopUp = view.findViewById(R.id.botonPopUp);

        tituloPopUp.setText(alimento.getNombre());

        double cal = alimento.getCalorias();
        double pro = alimento.getProteinas();
        double car = alimento.getCarbohidratos();
        double gra = alimento.getGrasas();

        // Usamos %.2f para mostrar solo 2 decimales
        String detalles = String.format(
                "Calorías \uD83D\uDD25 : %.2f kcal/100g\n" +
                        "Proteínas \uD83E\uDD69 : %.2f g/100g\n" +
                        "Carbohidratos \uD83C\uDF5A : %.2f g/100g\n" +
                        "Grasas \uD83C\uDF6B : %.2f g/100g",
                cal, pro, car, gra
        );

        detallesPopUp.setText(detalles);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonPopUp.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void mostrarPopupPropios(AlimentoDTO alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.detalles_comida, null);
        builder.setView(view);

        TextView tituloPopUp = view.findViewById(R.id.tituloPopUp);
        TextView detallesPopUp = view.findViewById(R.id.detallesPopUp);
        Button botonPopUp = view.findViewById(R.id.botonPopUp);

        tituloPopUp.setText(alimento.getNombre());

        double cal = alimento.getCalorias();
        double pro = alimento.getProteinas();
        double car = alimento.getCarbohidratos();
        double gra = alimento.getGrasas();

        // Usamos %.2f para mostrar solo 2 decimales
        String detalles = String.format(
                "Calorías \uD83D\uDD25 : %.2f kcal/100g\n" +
                        "Proteínas \uD83E\uDD69 : %.2f g/100g\n" +
                        "Carbohidratos \uD83C\uDF5A : %.2f g/100g\n" +
                        "Grasas \uD83C\uDF6B : %.2f g/100g",
                cal, pro, car, gra
        );


        detallesPopUp.setText(detalles);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonPopUp.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void mostrarPopupActualizar(AlimentoDTO alimento) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.editar_alimento, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editCalorias = view.findViewById(R.id.editCalorias);
        EditText editProteinas = view.findViewById(R.id.editProteinas);
        EditText editCarbos = view.findViewById(R.id.editCarbos);
        EditText editGrasas = view.findViewById(R.id.editGrasas);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);

        editNombre.setText(alimento.getNombre());
        editCalorias.setText(String.valueOf(alimento.getCalorias()));
        editProteinas.setText(String.valueOf(alimento.getProteinas()));
        editCarbos.setText(String.valueOf(alimento.getCarbohidratos()));
        editGrasas.setText(String.valueOf(alimento.getGrasas()));

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevoNombre = editNombre.getText().toString();
                float nuevasCalorias = Float.parseFloat(editCalorias.getText().toString());
                float nuevasProteinas = Float.parseFloat(editProteinas.getText().toString());
                float nuevosCarbos = Float.parseFloat(editCarbos.getText().toString());
                float nuevasGrasas = Float.parseFloat(editGrasas.getText().toString());

                AlimentoDTO alimentoEditado = new AlimentoDTO();
                alimentoEditado.setIdAlimento(alimento.getIdAlimento());
                alimentoEditado.setNombre(nuevoNombre);
                alimentoEditado.setCalorias(nuevasCalorias);
                alimentoEditado.setProteinas(nuevasProteinas);
                alimentoEditado.setCarbohidratos(nuevosCarbos);
                alimentoEditado.setGrasas(nuevasGrasas);

                actualizarAlimentoConfirmado(alimentoEditado, dialogoActual);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce números válidos", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    public void mostrarPopUpBorrar(AlimentoDTO alimentoDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        pregunta.setText("¿Deseas borrar el alimento " + alimentoDTO.getNombre() + "?");

        Button cancelar = view.findViewById(R.id.botonCancelarBorrar);
        cancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setOnClickListener(v -> {
            borrarAlimentoConfirmado(alimentoDTO);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }

        dialog.show();
    }

        private void cargarDatos() {
        if (idUsuario == null || idUsuario == -1L) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_LONG).show();
        }

        alimentosViewModel.obtenerLosAlimentos(idUsuario);
    }

    private void borrarAlimentoConfirmado(AlimentoDTO alimento) {
        if (alimento.getIdAlimento() == null) {
            Toast.makeText(this, "Error: El alimento no tiene ID", Toast.LENGTH_SHORT).show();
            return;
        }

        alimentosViewModel.borrarAlimento(alimento.getIdAlimento(), idUsuario);
    }

    private void actualizarAlimentoConfirmado(AlimentoDTO alimento, AlertDialog dialog) {
        if (alimento.getIdAlimento() == null) {
            Toast.makeText(this, "Error: El alimento no tiene ID", Toast.LENGTH_SHORT).show();
            return;
        }

        alimentosViewModel.actualizarAlimento(alimento.getIdAlimento(), idUsuario, alimento);
    }

    private void mostrarPopUpCrearAlimento() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.editar_alimento, null);
        builder.setView(view);

        TextView titulo = view.findViewById(R.id.tituloEditarAlimento);
        EditText editNombre = view.findViewById(R.id.editNombre);
        EditText editCalorias = view.findViewById(R.id.editCalorias);
        EditText editProteinas = view.findViewById(R.id.editProteinas);
        EditText editCarbos = view.findViewById(R.id.editCarbos);
        EditText editGrasas = view.findViewById(R.id.editGrasas);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);

        titulo.setText("Crear Alimento");
        botonGuardar.setText("Crear");

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null) dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevoNombre = editNombre.getText().toString();
                float calorias = Float.parseFloat(editCalorias.getText().toString());
                float proteinas = Float.parseFloat(editProteinas.getText().toString());
                float carbohidratos = Float.parseFloat(editCarbos.getText().toString());
                float grasas = Float.parseFloat(editGrasas.getText().toString());

                AlimentoDTO alimentoCreado = new AlimentoDTO();
                alimentoCreado.setNombre(nuevoNombre);
                alimentoCreado.setCalorias(calorias);
                alimentoCreado.setProteinas(proteinas);
                alimentoCreado.setCarbohidratos(carbohidratos);
                alimentoCreado.setGrasas(grasas);

                alimentosViewModel.crearAlimento(idUsuario, alimentoCreado);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Por favor, introduce números válidos", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    private void observarAlimentos() {
        alimentosViewModel.getAlimentos().observe(this, lista -> {
            misAlimentosAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        alimentosViewModel.getMensajeError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }
        });

        alimentosViewModel.getAlimentoCreado().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Alimento creado correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        alimentosViewModel.getAlimentoActualizado().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Alimento actualizado correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        alimentosViewModel.getAlimentoBorrado().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Alimento eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}