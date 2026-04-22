package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ComidaSeguimientoDTO;
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;
import com.trabajo.fitnessapp.presentacion.adaptador.ComidasHoyAdapter;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.DietasViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Inicio.SeguimientoViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Perfil.PerfilViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentoInicio extends Fragment {

    private Long idUsuario;
    private ComidasHoyAdapter comidasHoyAdapter;
    private RecyclerView recyclerComidas;
    private SeguimientoViewModel seguimientoViewModel;
    private DietasViewModel dietasViewModel;
    private PerfilViewModel perfilViewModel;
    private Spinner spinnerDietas;
    private TextView txtCalorias, txtProteinas, txtCarbohidratos, txtGrasas, txtCaloriasObjetivo;
    private float calorias = 0;
    float caloriasObjetivoDia;
    private float proteinas = 0;
    float proteinasObjetivoDia;
    private float carbohidratos = 0;
    float carbohidratosObjetivoDia;
    private float grasas = 0;
    float grasasObjetivoDia;
    private List<DietaCompletaDTO> listaDietas = new ArrayList<>();
    private boolean primeraCarga = true;
    private ProgressBar progresoProteinas, progresoCarbohidratos, progresoGrasas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getLong("ID_USUARIO", -1L);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_inicio, container, false);

        enlazarVistas(view);
        configurarRecycler();

        comidasHoyAdapter.setOnItemClickListener(new ComidasHoyAdapter.OnItemClickListener() {
            @Override
            public void onCheckClick(ComidaSeguimientoDTO comidaSeguimientoDTO, boolean marcada) {
                if (marcada) {
                    seguimientoViewModel.registrarComidaRealizada(idUsuario, comidaSeguimientoDTO.getIdComida());
                    calorias += comidaSeguimientoDTO.getCaloriasTotales();
                    proteinas += comidaSeguimientoDTO.getProteinas();
                    carbohidratos += comidaSeguimientoDTO.getCarbohidratos();
                    grasas += comidaSeguimientoDTO.getGrasas();
                } else {
                    seguimientoViewModel.eliminarComidaRealizada(idUsuario, comidaSeguimientoDTO.getIdComida());
                    calorias -= comidaSeguimientoDTO.getCaloriasTotales();
                    proteinas -= comidaSeguimientoDTO.getProteinas();
                    carbohidratos -= comidaSeguimientoDTO.getCarbohidratos();
                    grasas -= comidaSeguimientoDTO.getGrasas();
                }
                actualizarContadores();
            }
        });

        spinnerDietas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (primeraCarga) {
                    primeraCarga = false;
                    return;
                }

                DietaCompletaDTO dietaSeleccionada = listaDietas.get(position);

                dietasViewModel.activarDieta(idUsuario, dietaSeleccionada.getIdDietaCompleta());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        seguimientoViewModel = new ViewModelProvider(this).get(SeguimientoViewModel.class);
        dietasViewModel = new ViewModelProvider(this).get(DietasViewModel.class);
        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        perfilViewModel.cargarPerfil(idUsuario);
        observarSeguimientoViewModel();

        if (idUsuario != null && idUsuario != -1L) {
            seguimientoViewModel.obtenerComidasHoy(idUsuario);
        }

        dietasViewModel.obtenerLasDietas(idUsuario);

        return view;
    }


    private void enlazarVistas(View view) {
        recyclerComidas = view.findViewById(R.id.recyclerComidas);
        txtCalorias = view.findViewById(R.id.txtCaloriasResumen);
        txtCarbohidratos = view.findViewById(R.id.txtCarbohidratosResumen);
        txtGrasas = view.findViewById(R.id.txtGrasasResumen);
        txtProteinas = view.findViewById(R.id.txtProteinasResumen);
        spinnerDietas = view.findViewById(R.id.spinnerDietas);
        progresoProteinas = view.findViewById(R.id.progresoProteinas);
        progresoCarbohidratos = view.findViewById(R.id.progresoCarbohidratos);
        progresoGrasas = view.findViewById(R.id.progresoGrasas);
        txtCaloriasObjetivo = view.findViewById(R.id.txtCaloriasObjetivo);
    }

    private void configurarRecycler() {
        comidasHoyAdapter = new ComidasHoyAdapter();
        recyclerComidas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerComidas.setAdapter(comidasHoyAdapter);
    }

    private void calcularObjetivosDelDia(List<ComidaSeguimientoDTO> comidas) {
        caloriasObjetivoDia = 0;
        proteinasObjetivoDia = 0;
        carbohidratosObjetivoDia = 0;
        grasasObjetivoDia = 0;

        for (ComidaSeguimientoDTO comida : comidas) {
            caloriasObjetivoDia += comida.getCaloriasTotales();
            proteinasObjetivoDia += comida.getProteinas();
            carbohidratosObjetivoDia += comida.getCarbohidratos();
            grasasObjetivoDia += comida.getGrasas();
        }
    }

    private void cargarSpinner(List<DietaCompletaDTO> dietas) {

        listaDietas = dietas;

        List<String> nombres = new ArrayList<>();
        int posicionActiva = 0;

        for (int i = 0; i < dietas.size(); i++) {
            nombres.add(dietas.get(i).getNombre());

            if (dietas.get(i).isActiva()) {
                posicionActiva = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.spinner_despleagado,
                nombres
        );

        adapter.setDropDownViewResource(R.layout.spinner_despleagado);
        spinnerDietas.setAdapter(adapter);

        spinnerDietas.setSelection(posicionActiva);
    }

    private void actualizarContadores() {
        txtCalorias.setText(String.format("%.2f / %.2f", limpiar(calorias), caloriasObjetivoDia));
        txtProteinas.setText(String.format("%.2f / %.2fg", limpiar(proteinas), proteinasObjetivoDia));
        txtCarbohidratos.setText(String.format("%.2f / %.2fg", limpiar(carbohidratos), carbohidratosObjetivoDia));
        txtGrasas.setText(String.format("%.2f / %.2fg", limpiar(grasas), grasasObjetivoDia));

        progresoProteinas.setProgress(calcularPorcentaje(proteinas, proteinasObjetivoDia));
        progresoCarbohidratos.setProgress(calcularPorcentaje(carbohidratos, carbohidratosObjetivoDia));
        progresoGrasas.setProgress(calcularPorcentaje(grasas, grasasObjetivoDia));
    }

    private int calcularPorcentaje(float actual, float objetivo) {
        if (objetivo == 0) {
            return 0;
        }

        int porcentaje = (int) ((actual / objetivo) * 100);

        return Math.min(porcentaje, 100);
    }

    private float limpiar(float valor) {
        return Math.abs(valor) < 0.0001f ? 0f : valor;
    }

    private void observarSeguimientoViewModel() {
        seguimientoViewModel.getComidasExito().observe(getViewLifecycleOwner(), comidas -> {
            if (comidas != null) {
                comidasHoyAdapter.setLista(comidas);
                calcularObjetivosDelDia(comidas);

                // Reset cada vez que se actualiza el fragmento
                calorias = 0;
                proteinas = 0;
                carbohidratos = 0;
                grasas = 0;

                for (ComidaSeguimientoDTO comida : comidas) {
                    if (comida.isRegistrada()) {
                        calorias += comida.getCaloriasTotales();
                        proteinas += comida.getProteinas();
                        carbohidratos += comida.getCarbohidratos();
                        grasas += comida.getGrasas();
                    }
                }
                actualizarContadores();
            }
        });

        seguimientoViewModel.getComidaRegistradaExito().observe(getViewLifecycleOwner(), registro -> {
            if (registro != null) {
                Toast.makeText(getContext(), "Comida registrada", Toast.LENGTH_SHORT).show();
            }
        });

        seguimientoViewModel.getComidaRealizadaBorradaExito().observe(getViewLifecycleOwner(), registro -> {
            if (registro != null) {
                Toast.makeText(getContext(), "Comida eliminada", Toast.LENGTH_SHORT).show();
            }
        });

        seguimientoViewModel.getMensajeError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        dietasViewModel.getActivarDieta().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {

                calorias = 0;
                proteinas = 0;
                carbohidratos = 0;
                grasas = 0;

                seguimientoViewModel.obtenerComidasHoy(idUsuario);
            }
        });

        dietasViewModel.getDietas().observe(getViewLifecycleOwner(), dietas -> {
            if (dietas != null) {
                cargarSpinner(dietas);
            }
        });

        perfilViewModel.getPerfilExito().observe(getViewLifecycleOwner(), perfil -> {
            if (perfil != null) {

                caloriasObjetivoDia = perfil.getCaloriasObjetivo();

                txtCaloriasObjetivo.setText(caloriasObjetivoDia + " kcal");

                actualizarContadores();
            }
        });
    }
}
