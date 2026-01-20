package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;
import com.trabajo.fitnessapp.datos.Utils.MusculosFlitro;
import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;
import com.trabajo.fitnessapp.dominio.Musculo;
import com.trabajo.fitnessapp.presentacion.adaptador.EjerciciosAdapter;

import java.util.ArrayList;
import java.util.List;

public class MenuEjerciciosActivity extends AppCompatActivity {
    private List<ApiEjercicioDTO> listaMusculos = new ArrayList<>();
    private ImageButton botonVolverActividad;
    private Spinner spinnerMusculos;
    private RecyclerView ejerciciosRecyclerView;
    private EjerciciosAdapter ejerciciosAdapter;
    private EjerciciosViewModel viewModel;
    private Long idUsuario;

    private Long idDiaEnRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_ejercicios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1l);
        viewModel = new ViewModelProvider(this).get(EjerciciosViewModel.class);

        enlazarVistas();
        configurarSpinner();
        configurarBotones();
        configurarRecyclerView();
        observarEjercicios();
    }

    private void enlazarVistas() {
        botonVolverActividad = findViewById(R.id.botonVolverActividad);
        ejerciciosRecyclerView = findViewById(R.id.recyclerEjercicios);
        spinnerMusculos = findViewById(R.id.spinnerMusculos);
    }

    private void configurarRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        ejerciciosRecyclerView.setLayoutManager(gridLayoutManager);

        ejerciciosAdapter = new EjerciciosAdapter();
        ejerciciosRecyclerView.setAdapter(ejerciciosAdapter);

        idDiaEnRutina = getIntent().getLongExtra("ID_DIA_EN_RUTINA", -1);

        if (idDiaEnRutina != -1) {
            ejerciciosAdapter.setMostrarBotonAñadir(true);
        } else {
            ejerciciosAdapter.setMostrarBotonAñadir(false);
        }
        ejerciciosAdapter.setOnItemClickListener(new EjerciciosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ApiEjercicioDTO ejercicioDTO) {
                mostrarDetalles(ejercicioDTO);
            }

            @Override
            public void onAñadirEjercicio(ApiEjercicioDTO apiEjercicioDTO) {
                if (idDiaEnRutina != -1) {

                    EjercicioDTO ejercicio = convertirApiEjercicioEnEjercicio(apiEjercicioDTO);
                    viewModel.anhadirEjercicio(idDiaEnRutina, ejercicio);
                    System.out.println("Hola");
                }
            }
        });

        cargarEjercicios();
    }

    private void configurarSpinner() {

        List<String> musculos = new ArrayList<>(MusculosFlitro.MUSCULOS.values());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_no_desplegado,
                musculos
        );

        adapter.setDropDownViewResource(R.layout.spinner_despleagado);
        spinnerMusculos.setAdapter(adapter);

        spinnerMusculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarPorMusculo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private EjercicioDTO convertirApiEjercicioEnEjercicio(ApiEjercicioDTO apiEjercicio) {
        EjercicioDTO ejercicio = new EjercicioDTO();
        ejercicio.setIdApi(apiEjercicio.getId());
        ejercicio.setNombre(apiEjercicio.getName());
        if (apiEjercicio.getPrimaryMuscles() != null && !apiEjercicio.getPrimaryMuscles().isEmpty()) {
            Musculo musculo = MusculosFlitro.mapearMusculoEnum(apiEjercicio.getPrimaryMuscles().get(0));
            ejercicio.setMusculoEnfocado(musculo);
        }
        return ejercicio;
    }

    private void filtrarPorMusculo(int posicion) {

        String clave = new ArrayList<>(MusculosFlitro.MUSCULOS.keySet()).get(posicion);

        if (clave.equals("all")) {
            ejerciciosAdapter.setLista(listaMusculos);
            return;
        }

        List<ApiEjercicioDTO> filtrados = new ArrayList<>();

        for (ApiEjercicioDTO e : listaMusculos) {
            if (e.getPrimaryMuscles() == null) continue;

            else if (e.getPrimaryMuscles().contains(clave)) {
                filtrados.add(e);
            }
        }

        ejerciciosAdapter.setLista(filtrados);
    }




    private void configurarBotones() {
        botonVolverActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cargarEjercicios() {
        viewModel.getEjerciciosGlobales().observe(this, lista -> {
            if (lista != null && !lista.isEmpty()) {
                listaMusculos = lista;
                ejerciciosAdapter.setLista(lista);
            }
        });
    }

    private void mostrarDetalles(ApiEjercicioDTO ejercicio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.detalles_ejercicio, null);
        builder.setView(view);

        TextView titulo = view.findViewById(R.id.tituloPopUpEjercicio);
        TextView musculoTexto = view.findViewById(R.id.musculoPopUp);
        ImageView imagen = view.findViewById(R.id.imagenEjercicio);
        ImageButton botonAnterior = view.findViewById(R.id.botonAnterior);
        ImageButton botonSiguiente = view.findViewById(R.id.botonSiguiente);
        Button botonCerrar = view.findViewById(R.id.botonCerrarPopup);

        titulo.setText(ejercicio.getName());

        String musculo = "No especificado";

        if (ejercicio.getPrimaryMuscles() != null && !ejercicio.getPrimaryMuscles().isEmpty()) {
            musculo = MusculosFlitro.traducirMusculo(
                    ejercicio.getPrimaryMuscles().get(0)
            );
        }

        musculoTexto.setText("Músculo: " + musculo);

        // IMÁGENES
        List<String> imagenes = ejercicio.getImages();
        final int[] indiceActual = {0};

        Glide.with(this)
                .load(imagenes.get(0))
                .into(imagen);

        botonSiguiente.setOnClickListener(v -> {
            indiceActual[0] = (indiceActual[0] + 1) % imagenes.size();
            Glide.with(this)
                    .load(imagenes.get(indiceActual[0]))
                    .into(imagen);
        });

        botonAnterior.setOnClickListener(v -> {
            indiceActual[0]--;
            if (indiceActual[0] < 0) {
                indiceActual[0] = imagenes.size() - 1;
            }
            Glide.with(this)
                    .load(imagenes.get(indiceActual[0]))
                    .into(imagen);
        });

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        botonCerrar.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void observarEjercicios() {
        viewModel.getEjercicioAnhadido().observe(this, exito -> {
            if (exito != null && exito) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ID_DIA_EN_RUTINA", idDiaEnRutina);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Ejercicio añadido correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getMensajeError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }


}