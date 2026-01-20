package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.ComidaDTO;
import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;
import com.trabajo.fitnessapp.datos.Utils.ModoIngrediente;
import com.trabajo.fitnessapp.presentacion.adaptador.ComidasAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.DiasDietaAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.DietasAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.IngredientesAdapter;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.ComidasViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.CrearDietaActivity;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.DiasEnDietaViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.DietasViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.MenuIngredientesActivity;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.IngredientesViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.MenuAlimentosActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentoDietas extends Fragment {
    private Button botonInformacionAlimentos, botonEditarNombreDia, botonBorrarDia;
    private ImageButton botonCrearDieta;
    private Long idUsuario;
    private DietasAdapter dietasAdapter;
    private DiasDietaAdapter diasAdapter;
    private ComidasAdapter comidasAdapter;
    private IngredientesAdapter ingredientesAdapter;
    private RecyclerView recyclerViewDietas, recyclerViewDias, recyclerViewComidas;
    private DietasViewModel dietasViewModel;
    private DiasEnDietaViewModel diasViewModel;
    private ComidasViewModel comidasViewModel;
    private IngredientesViewModel ingredientesViewModel;
    private TextView nombreDia;
    private DietaCompletaDTO dietaActual;
    private DiaEnDietaDTO diaActual;
    private ComidaDTO comidaActual;
    AlertDialog dialogoActual;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getLong("ID_USUARIO", -1L);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_dietas, container, false);

        enlazarVistas(view);

        LinearLayoutManager layoutManagerDietas = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerDias = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerComidas = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewDietas.setLayoutManager(layoutManagerDietas);
        recyclerViewDias.setLayoutManager(layoutManagerDias);
        recyclerViewComidas.setLayoutManager(layoutManagerComidas);

        dietasAdapter = new DietasAdapter();
        diasAdapter = new DiasDietaAdapter();
        comidasAdapter = new ComidasAdapter();
        ingredientesAdapter = new IngredientesAdapter();

        recyclerViewDietas.setAdapter(dietasAdapter);
        recyclerViewDias.setAdapter(diasAdapter);
        recyclerViewComidas.setAdapter(comidasAdapter);

        recyclerViewDias.setVisibility(View.GONE);
        recyclerViewComidas.setVisibility(View.GONE);

        botonBorrarDia.setOnClickListener(v -> mostrarPopUpBorrarDia(diaActual));
        botonEditarNombreDia.setOnClickListener(v -> mostrarEditarDia(diaActual));

        dietasAdapter.setOnDietaClickListener(new DietasAdapter.OnDietaClickListener() {

            @Override
            public void onMostrarPlanClick(DietaCompletaDTO dieta) {
                dietaActual = dieta;
                Long idDietaSeleccionada = dieta.getIdDietaCompleta();
                recyclerViewDias.setVisibility(VISIBLE);
                cargarDias(idDietaSeleccionada);
            }

            @Override
            public void onEditarClick(DietaCompletaDTO dieta) {
                Intent intent = new Intent(requireContext(), CrearDietaActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("DIETA", dieta);
                startActivity(intent);
            }

            @Override
            public void onBorrarClick(DietaCompletaDTO dieta) {
                mostrarPopUpBorrar(dieta);
            }
        });

        diasAdapter.setOnDiaClickListener(new DiasDietaAdapter.OnDiaClickListener() {
            @Override
            public void onDiaClick(DiaEnDietaDTO dia) {
                mostrarDetalleDia(dia);
            }

            @Override
            public void onAddDiaClick() {
                mostrarCrearDia();
            }
        });

        comidasAdapter.setOnComidaClickListener(new ComidasAdapter.OnComidaClickListener() {
            @Override
            public void onEditarComida(ComidaDTO comida) {
                mostrarEditarComida(comida);
            }

            @Override
            public void onBorrarComida(ComidaDTO comida) {
                mostrarPopUpBorrarComida(comida);
            }

            @Override
            public void onAddComidaClick() {
                mostrarPopUpCrearComida();
            }

            @Override
            public void onVerIngredientes(ComidaDTO comida) {
                comidaActual = comida;
                mostrarPopUpIngredientes(comida);
            }
        });

        dietasViewModel = new ViewModelProvider(this).get(DietasViewModel.class);
        diasViewModel = new ViewModelProvider(this).get(DiasEnDietaViewModel.class);
        comidasViewModel = new ViewModelProvider(this).get(ComidasViewModel.class);
        ingredientesViewModel = new ViewModelProvider(this).get(IngredientesViewModel.class);

        configurarBotones();
        observarDietas();
        observarDias();
        observarComidas();
        observarIngredientes();

        return view;
    }

    private void enlazarVistas(View view) {
        botonInformacionAlimentos = view.findViewById(R.id.botonInformacionAlimentos);
        botonCrearDieta = view.findViewById(R.id.botonCrearDieta);
        recyclerViewDietas = view.findViewById(R.id.recyclerDietas);
        recyclerViewDias = view.findViewById(R.id.recyclerDias);
        recyclerViewComidas = view.findViewById(R.id.recyclerComidas);
        nombreDia = view.findViewById(R.id.nombreDia);
        botonBorrarDia = view.findViewById(R.id.botonBorrarDia);
        botonEditarNombreDia = view.findViewById(R.id.botonEditarDia);
    }

    private void configurarBotones() {
        botonInformacionAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MenuAlimentosActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });

        botonCrearDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CrearDietaActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDietas();

        if (dietaActual != null) {
            cargarDias(dietaActual.getIdDietaCompleta());
        }

        if (diaActual != null) {
            cargarComidas(diaActual.getIdDiaEnDieta());
        }

        if (comidaActual != null) {
            cargarIngredientes(comidaActual.getIdComida());
        }
    }

    private void cargarDietas() {
        dietasViewModel.obtenerLasDietas(idUsuario);
    }

    private void cargarDias(Long idDietaCompleta) {
        diasViewModel.obtenerLosDias(idDietaCompleta);
    }

    private void cargarComidas(Long idDiaEnDieta) {
        comidasViewModel.obtenerComidas(idDiaEnDieta);
    }

    public void mostrarPopUpBorrar(DietaCompletaDTO dietaCompletaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);

        pregunta.setText("¿Deseas borrar la dieta " + dietaCompletaDTO.getNombre() + "?");
        titulo.setText("Borrar Dieta");

        Button cancelar = view.findViewById(R.id.botonCancelarBorrar);
        cancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setOnClickListener(v -> {
            borrarDietaConfirmado(dietaCompletaDTO);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }

        dialog.show();
    }

    private void borrarDietaConfirmado(DietaCompletaDTO dietaCompletaDTO) {
        dietasViewModel.borrarDieta(dietaCompletaDTO.getIdDietaCompleta(), idUsuario);
    }

    private void mostrarDetalleDia(DiaEnDietaDTO dia) {
        nombreDia.setVisibility(View.VISIBLE);
        nombreDia.setText(dia.getNombre().toUpperCase());
        botonEditarNombreDia.setVisibility(VISIBLE);
        botonBorrarDia.setVisibility(VISIBLE);
        diaActual = dia;
        recyclerViewComidas.setVisibility(VISIBLE);
        cargarComidas(dia.getIdDiaEnDieta());
    }

    public void mostrarPopUpBorrarDia(DiaEnDietaDTO diaEnDietaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);
        titulo.setText("Borrar Dia");
        pregunta.setText("¿Deseas borrar el dia " + diaEnDietaDTO.getNombre() + "?");

        Button cancelar = view.findViewById(R.id.botonCancelarBorrar);
        cancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setOnClickListener(v -> {
            borrarDiaConfirmado(diaEnDietaDTO);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }

        dialog.show();
    }

    private void borrarDiaConfirmado(DiaEnDietaDTO diaEnDietaDTO) {

        Long idDietaCompleta = dietaActual.getIdDietaCompleta();

        diasViewModel.borrarDia(diaEnDietaDTO.getIdDiaEnDieta(), idDietaCompleta);
    }

    private void mostrarEditarDia(DiaEnDietaDTO diaEnDietaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.editar_dia, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombre);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);
        editNombre.setText(diaEnDietaDTO.getNombre());

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevoNombre = editNombre.getText().toString();

                DiaEnDietaDTO diaEditado = new DiaEnDietaDTO();
                diaEditado.setIdDiaEnDieta(diaEnDietaDTO.getIdDiaEnDieta());
                diaEditado.setNombre(nuevoNombre);
                diaEditado.setGrasas(diaEnDietaDTO.getGrasas());
                diaEditado.setProteinas(diaEnDietaDTO.getProteinas());
                diaEditado.setCarbohidratos(diaEnDietaDTO.getCarbohidratos());
                diaEditado.setCaloriasTotales(diaEnDietaDTO.getCaloriasTotales());
                diaEditado.setDiaDeLaSemana(diaEnDietaDTO.getDiaDeLaSemana());

                actualizarDiaConfirmado(diaEditado);

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Error al editar", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    private void actualizarDiaConfirmado(DiaEnDietaDTO diaEditado) {

        Long idDietaCompleta = dietaActual.getIdDietaCompleta();

        diasViewModel.editarDia(diaEditado.getIdDiaEnDieta(), idDietaCompleta, diaEditado);
    }

    private void mostrarCrearDia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.crear_dia, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombreNuevoDia);
        Spinner spinnerDia = view.findViewById(R.id.spinnerDiaSemana);
        Button botonGuardar = view.findViewById(R.id.botonGuardarNuevoDia);
        Button botonCancelar = view.findViewById(R.id.botonCancelarNuevoDia);

        List<String> diasOpciones = new ArrayList<>();
        diasOpciones.add("Día de la semana");
        for (DiaDeLaSemana dia : DiaDeLaSemana.values()) {
            diasOpciones.add(dia.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, diasOpciones) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#757474"));
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                view.setBackgroundColor(Color.parseColor("#1E1E1E"));
                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#757474"));
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(adapter);
        spinnerDia.setSelection(0);

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();
            String diaSemanaSeleccionado = spinnerDia.getSelectedItem().toString();

            if (diaSemanaSeleccionado.equals("Día de la semana")) {
                Toast.makeText(getContext(), "Seleciona un dia", Toast.LENGTH_SHORT).show();
                return;
            }

            DiaEnDietaDTO nuevoDia = new DiaEnDietaDTO();
            nuevoDia.setNombre(nombre);
            nuevoDia.setDiaDeLaSemana(DiaDeLaSemana.valueOf(diaSemanaSeleccionado));
            nuevoDia.setCaloriasTotales(0);
            nuevoDia.setProteinas(0);
            nuevoDia.setCarbohidratos(0);
            nuevoDia.setGrasas(0);

            crearDiaConfirmado(nuevoDia);
        });

        dialogoActual.show();
    }

    private void crearDiaConfirmado(DiaEnDietaDTO nuevoDia) {
        Long idDieta = dietaActual.getIdDietaCompleta();

        diasViewModel.crearDia(idDieta, nuevoDia);
    }

    private void mostrarPopUpCrearComida() {

        if (diaActual == null) {
            Toast.makeText(getContext(), "Selecciona un día primero", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.crear_dia, null);
        builder.setView(view);

        TextView titulo = view.findViewById(R.id.tituloCrearDia);
        titulo.setText("Añadir Nueva Comida");

        EditText editNombre = view.findViewById(R.id.editNombreNuevoDia);

        // Spinner fuera
        Spinner spinner = view.findViewById(R.id.spinnerDiaSemana);
        if (spinner != null) spinner.setVisibility(View.GONE);

        Button botonGuardar = view.findViewById(R.id.botonGuardarNuevoDia);
        Button botonCancelar = view.findViewById(R.id.botonCancelarNuevoDia);

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            String nombre = editNombre.getText().toString().trim();

            ComidaDTO nuevaComida = new ComidaDTO();
            nuevaComida.setNombre(nombre);
            nuevaComida.setCaloriasTotales(0);
            nuevaComida.setProteinas(0);
            nuevaComida.setCarbohidratos(0);
            nuevaComida.setGrasas(0);

            crearComidaConfirmado(nuevaComida);
        });

    dialogoActual.show();

    }

    private void crearComidaConfirmado(ComidaDTO comida) {
        Long idDiaEnDieta = diaActual.getIdDiaEnDieta();

        comidasViewModel.crearComida(idDiaEnDieta, comida);
    }

    private void mostrarEditarComida(ComidaDTO comidaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.editar_dia, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombre);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);
        editNombre.setText(comidaDTO.getNombre());

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevoNombre = editNombre.getText().toString();

                ComidaDTO comidaEditada = new ComidaDTO();
                comidaEditada.setIdComida(comidaDTO.getIdComida());
                comidaEditada.setNombre(nuevoNombre);
                comidaEditada.setGrasas(comidaDTO.getGrasas());
                comidaEditada.setProteinas(comidaDTO.getProteinas());
                comidaEditada.setCarbohidratos(comidaDTO.getCarbohidratos());
                comidaEditada.setCaloriasTotales(comidaDTO.getCaloriasTotales());

                actualizarComidaConfirmada(comidaEditada);

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Error al editar", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    private void actualizarComidaConfirmada(ComidaDTO comidaEditada) {

        Long idDiaEnDieta = diaActual.getIdDiaEnDieta();

        comidasViewModel.editarComida(comidaEditada.getIdComida(), idDiaEnDieta, comidaEditada);
    }

    private void mostrarPopUpBorrarComida(ComidaDTO comida) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);

        titulo.setText("Borrar comida");
        pregunta.setText("¿Deseas borrar " + comida.getNombre() + "?");

        view.findViewById(R.id.botonCancelarBorrar)
                .setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.botonConfirmarBorrar)
                .setOnClickListener(v -> {
                    borrarComidaConfirmado(comida);
                    dialog.dismiss();
                });

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void borrarComidaConfirmado(ComidaDTO comida) {
        comidasViewModel.borrarComida(
                comida.getIdComida(),
                diaActual.getIdDiaEnDieta()
        );
    }

    private void mostrarPopUpIngredientes(ComidaDTO comidaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.lista_ingredientes, null);
        builder.setView(view);

        RecyclerView rvIngredientes = view.findViewById(R.id.recyclerIngredientes);
        rvIngredientes.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientesAdapter.setModo(ModoIngrediente.SOLO_LECTURA);
        rvIngredientes.setAdapter(ingredientesAdapter);


        Button botonCerrar = view.findViewById(R.id.botonCerrarPopup);
        Button botonModificar = view.findViewById(R.id.botonModificarComida);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCerrar.setOnClickListener(v -> dialog.dismiss());

        botonModificar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MenuIngredientesActivity.class);
            intent.putExtra("ID_COMIDA", comidaDTO.getIdComida());
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();

        cargarIngredientes(comidaDTO.getIdComida());
    }

    private void cargarIngredientes(Long idComida) {
        ingredientesViewModel.obtenerIngredientes(idComida);
    }

    private void observarDietas() {
        dietasViewModel.getDietas().observe(getViewLifecycleOwner(), lista -> {
            dietasAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        dietasViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        dietasViewModel.getDietaBorrada().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                // Si se estaba mostrando la dieta eliminada
                if (dietaActual != null) {
                    recyclerViewDias.setVisibility(View.GONE);
                    nombreDia.setVisibility(View.GONE);
                    botonEditarNombreDia.setVisibility(View.GONE);
                    botonBorrarDia.setVisibility(View.GONE);
                    dietaActual = null;
                    diaActual = null;
                    recyclerViewComidas.setVisibility(View.GONE);
                }
                Toast.makeText(getContext(), "Dieta eliminada correctamente", Toast.LENGTH_SHORT).show();
                dietasViewModel.obtenerLasDietas(idUsuario);
            }
        });
    }
    private void observarDias() {
        diasViewModel.getDias().observe(getViewLifecycleOwner(), lista -> {
            diasAdapter.setLista(lista != null ? lista : new ArrayList<>());
            // con este metodo  al borrar un dia se seleciona el dia anterior.
            if (lista != null && !lista.isEmpty()) {
                int nuevaPosicion = Math.min(diasAdapter.getPosicionActual(), lista.size() - 1);

                if (nuevaPosicion < 0 || nuevaPosicion >= lista.size()) {
                    nuevaPosicion = 0;
                }

                diasAdapter.setPosicionActual(nuevaPosicion);

                DiaEnDietaDTO diaSeleccionado = lista.get(nuevaPosicion);
                mostrarDetalleDia(diaSeleccionado);
            } else {
                // No quedan días
                diasAdapter.setPosicionActual(-1);
                diaActual = null;

                nombreDia.setVisibility(View.GONE);
                botonEditarNombreDia.setVisibility(View.GONE);
                botonBorrarDia.setVisibility(View.GONE);
                recyclerViewComidas.setVisibility(View.GONE);
            }
        });

        diasViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        diasViewModel.getDiaCreadoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día creado correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        diasViewModel.getDiaActualizadoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día actualizado correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        diasViewModel.getDiaBorradoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día eliminado correctamente", Toast.LENGTH_SHORT).show();
                cargarDietas();

                if (dietaActual != null) {
                    cargarDias(dietaActual.getIdDietaCompleta());
                }

                if (diaActual != null) {
                    cargarComidas(diaActual.getIdDiaEnDieta());
                }

                if (comidaActual != null) {
                    cargarIngredientes(comidaActual.getIdComida());
                }
            }
        });
    }

    private void observarComidas() {
        comidasViewModel.getComidas().observe(getViewLifecycleOwner(), lista -> {
            comidasAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        comidasViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        comidasViewModel.getComidaCreadaExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Comida creada correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        comidasViewModel.getComidaActualizadaExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Comida actualizada correctamente", Toast.LENGTH_SHORT).show();
                dialogoActual.dismiss();
            }
        });

        comidasViewModel.getComidaBorradaExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Comida eliminada correctamente", Toast.LENGTH_SHORT).show();
                cargarDietas();

                if (dietaActual != null) {
                    cargarDias(dietaActual.getIdDietaCompleta());
                }

                if (diaActual != null) {
                    cargarComidas(diaActual.getIdDiaEnDieta());
                }

                if (comidaActual != null) {
                    cargarIngredientes(comidaActual.getIdComida());
                }
            }
        });
    }

    private void observarIngredientes() {
        ingredientesViewModel.getIngredientes().observe(getViewLifecycleOwner(), lista -> {
                    ingredientesAdapter.setLista(lista != null ? lista : new ArrayList<>());
                });
    }
}






