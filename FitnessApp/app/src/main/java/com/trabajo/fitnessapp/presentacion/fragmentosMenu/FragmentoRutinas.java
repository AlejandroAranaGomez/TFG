package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;
import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;
import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;
import com.trabajo.fitnessapp.datos.dto.SerieDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;
import com.trabajo.fitnessapp.presentacion.adaptador.DiasRutinaAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.EjerciciosRutinaAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.RutinasAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.SeriesAdapter;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.CrearRutinaActivity;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.DiasEnRutinaViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.EjerciciosViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.MenuEjerciciosActivity;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.RutinasViewModel;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.SerieViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentoRutinas extends Fragment {

    private Long idUsuario;
    private Button botonInformacionEjercicios, botonEditarNombreDia, botonBorrarDia;
    private ImageButton botonCrearRutina;
    private RecyclerView recyclerViewRutinas, recyclerViewDias, recyclerViewEjercicios;
    private RutinasAdapter rutinasAdapter;
    private DiasRutinaAdapter diasRutinaAdapter;
    private EjerciciosRutinaAdapter ejerciciosRutinaAdapter;
    private SeriesAdapter seriesAdapter;
    private RutinasViewModel rutinasViewModel;
    private DiasEnRutinaViewModel diasEnRutinaViewModel;
    private EjerciciosViewModel ejerciciosViewModel;
    private SerieViewModel serieViewModel;
    private RutinaCompletaDTO rutinaActual;
    private DiaEnRutinaDTO diaActual;
    private EjercicioDTO ejercicioActual;
    private TextView nombreDia;
    AlertDialog dialogoActual;

    private int posicionAnterior = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getLong("ID_USUARIO", -1L);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_rutinas, container, false);

        enlazarVistas(view);
        configurarRecycler();

        botonEditarNombreDia.setOnClickListener(v -> mostrarEditarDia(diaActual));
        botonBorrarDia.setOnClickListener(v -> mostrarBorrarDia(diaActual));

        rutinasAdapter.setOnRutinaClickListener(new RutinasAdapter.OnRutinaClickListener() {
            @Override
            public void onVerDiasClick(RutinaCompletaDTO rutina) {
                diaActual = null;
                ejercicioActual = null;

                nombreDia.setVisibility(View.GONE);
                botonEditarNombreDia.setVisibility(View.GONE);
                botonBorrarDia.setVisibility(View.GONE);
                recyclerViewEjercicios.setVisibility(View.GONE);

                ejerciciosRutinaAdapter.setEjercicios(new ArrayList<>());

                // cargar nueva rutina
                rutinaActual = rutina;
                Long idRutinaSeleccionada = rutina.getIdRutinaCompleta();

                recyclerViewDias.setVisibility(View.VISIBLE);
                cargarDias(idRutinaSeleccionada);
            }

            @Override
            public void onEditarClick(RutinaCompletaDTO rutina) {
                Intent intent = new Intent(requireContext(), CrearRutinaActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("RUTINA", rutina);
                startActivity(intent);
            }

            @Override
            public void onBorrarClick(RutinaCompletaDTO rutina) {
                mostrarPopUpBorrar(rutina);
            }
        });

        diasRutinaAdapter.setOnDiaClickListener(new DiasRutinaAdapter.OnDiaClickListener() {
            @Override
            public void onDiaClick(DiaEnRutinaDTO dia) {
                posicionAnterior = diasRutinaAdapter.getPosicionActual();
                mostrarDetalleDia(dia);
            }

            @Override
            public void onAddDiaClick() {
                mostrarCrearDia();
            }
        });

        ejerciciosRutinaAdapter.setOnEjercicioClickListener(new EjerciciosRutinaAdapter.OnEjercicioClickListener() {
            @Override
            public void onBorrar(EjercicioDTO ejercicio) {
                mostrarPopUpBorrarEjercicio(ejercicio);
            }

            @Override
            public void onAnhadirEjercicio() {
                Intent intent = new Intent(getContext(), MenuEjerciciosActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                intent.putExtra("ID_RUTINA", rutinaActual.getIdRutinaCompleta());
                intent.putExtra("DIA_SEMANA", diaActual.getDiaDeLaSemana().name());
                startActivity(intent);
            }

            @Override
            public void onClick(EjercicioDTO ejercicio) {
                mostrarPopUpSeries(ejercicio);
            }
        });

        rutinasViewModel = new ViewModelProvider(this).get(RutinasViewModel.class);
        diasEnRutinaViewModel = new ViewModelProvider(this).get(DiasEnRutinaViewModel.class);
        ejerciciosViewModel = new ViewModelProvider(this).get(EjerciciosViewModel.class);
        serieViewModel = new ViewModelProvider(this).get(SerieViewModel.class);

        configurarBotones();
        observarRutinas();
        observarDias();
        observarEjercicios();
        observarSeries();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarRutinas();

        if (rutinaActual != null) {
            cargarDias(rutinaActual.getIdRutinaCompleta());
        }

        if (diaActual != null && rutinaActual != null) {
            cargarEjercicios(rutinaActual.getIdRutinaCompleta(), diaActual.getDiaDeLaSemana());
        }
    }

    private void enlazarVistas(View view) {
        botonInformacionEjercicios = view.findViewById(R.id.botonInformacionEjercicios);
        recyclerViewRutinas = view.findViewById(R.id.recyclerRutinas);
        recyclerViewDias = view.findViewById(R.id.recyclerDias);
        recyclerViewEjercicios = view.findViewById(R.id.recyclerEjercicios);
        botonCrearRutina = view.findViewById(R.id.botonCrearRutina);
        botonBorrarDia = view.findViewById(R.id.botonBorrarDia);
        botonEditarNombreDia = view.findViewById(R.id.botonEditarDia);
        nombreDia = view.findViewById(R.id.nombreDia);
    }

    private void configurarBotones() {
        botonInformacionEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MenuEjerciciosActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });

        botonCrearRutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), CrearRutinaActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });
    }

    private void configurarRecycler() {
        ejerciciosRutinaAdapter = new EjerciciosRutinaAdapter();

        LinearLayoutManager layoutManagerRutinas = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRutinas.setLayoutManager(layoutManagerRutinas);
        rutinasAdapter = new RutinasAdapter();
        recyclerViewRutinas.setAdapter(rutinasAdapter);

        LinearLayoutManager layoutManagerDias = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDias.setLayoutManager(layoutManagerDias);
        diasRutinaAdapter = new DiasRutinaAdapter();
        recyclerViewDias.setAdapter(diasRutinaAdapter);
        recyclerViewDias.setVisibility(View.GONE);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = ejerciciosRutinaAdapter.getItemViewType(position);

                if (viewType == EjerciciosRutinaAdapter.BOTON_ANHADIR) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerViewEjercicios.setLayoutManager(gridLayoutManager);
        recyclerViewEjercicios.setAdapter(ejerciciosRutinaAdapter);
        recyclerViewEjercicios.setVisibility(View.GONE);

        seriesAdapter = new SeriesAdapter();
    }

    private void mostrarDetalleDia(DiaEnRutinaDTO dia) {
        nombreDia.setVisibility(View.VISIBLE);
        nombreDia.setText(dia.getNombre().toUpperCase());
        botonEditarNombreDia.setVisibility(VISIBLE);
        botonBorrarDia.setVisibility(VISIBLE);
        diaActual = dia;
        recyclerViewEjercicios.setVisibility(VISIBLE);
        cargarEjercicios(rutinaActual.getIdRutinaCompleta(), diaActual.getDiaDeLaSemana());
    }

    public void mostrarPopUpBorrar(RutinaCompletaDTO rutinaCompletaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);

        pregunta.setText("¿Deseas borrar la rutina " + rutinaCompletaDTO.getNombreRutinaCompleta() + "?");
        titulo.setText("Borrar Rutina");

        Button cancelar = view.findViewById(R.id.botonCancelarBorrar);
        cancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.fondoContenedorRutinas)
        );
        confirmar.setOnClickListener(v -> {
            borrarRutinaConfirmado(rutinaCompletaDTO);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }

        dialog.show();
    }

    private void mostrarCrearDia() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.crear_dia, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombreNuevoDia);
        Spinner spinnerDia = view.findViewById(R.id.spinnerDiaSemana);
        Button botonGuardar = view.findViewById(R.id.botonGuardarNuevoDia);
        Button botonCancelar = view.findViewById(R.id.botonCancelarNuevoDia);
        TextView textoDias = view.findViewById(R.id.textoCrearDia);

        textoDias.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.BotonesImagenesRutinas)
        );
        botonGuardar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.BotonesImagenesRutinas));

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

            DiaEnRutinaDTO nuevoDia = new DiaEnRutinaDTO();
            nuevoDia.setNombre(nombre);
            nuevoDia.setDiaDeLaSemana(DiaDeLaSemana.valueOf(diaSemanaSeleccionado));

            crearDiaConfirmado(nuevoDia);
        });

        dialogoActual.show();
    }

    private void crearDiaConfirmado(DiaEnRutinaDTO nuevoDia) {
        Long idRutina = rutinaActual.getIdRutinaCompleta();
        diasEnRutinaViewModel.guardaDia(nuevoDia.getDiaDeLaSemana() ,idRutina, nuevoDia);
    }

    private void mostrarEditarDia(DiaEnRutinaDTO diaEnRutinaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.editar_dia, null);
        builder.setView(view);

        EditText editNombre = view.findViewById(R.id.editNombre);
        Button botonGuardar = view.findViewById(R.id.botonGuardarEditar);
        Button botonCancelar = view.findViewById(R.id.botonCancelarEditar);
        TextView nombreEditarDia = view.findViewById(R.id.textoNombreEditarDia);
        editNombre.setText(diaEnRutinaDTO.getNombre());

        nombreEditarDia.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.BotonesImagenesRutinas)
        );
        botonGuardar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.BotonesImagenesRutinas));

        dialogoActual = builder.create();
        if (dialogoActual.getWindow() != null)
            dialogoActual.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        botonCancelar.setOnClickListener(v -> dialogoActual.dismiss());

        botonGuardar.setOnClickListener(v -> {
            try {

                String nuevoNombre = editNombre.getText().toString();

                DiaEnRutinaDTO diaEditado = new DiaEnRutinaDTO();
                diaEditado.setIdDiaEnRutina(diaEnRutinaDTO.getIdDiaEnRutina());
                diaEditado.setNombre(nuevoNombre);
                diaEditado.setDiaDeLaSemana(diaEnRutinaDTO.getDiaDeLaSemana());

                actualizarDiaConfirmado(diaEditado);

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Error al editar", Toast.LENGTH_SHORT).show();
            }
        });
        dialogoActual.show();
    }

    private void actualizarDiaConfirmado(DiaEnRutinaDTO diaEditado) {

        Long idRutinaCompleta = rutinaActual.getIdRutinaCompleta();

        diasEnRutinaViewModel.guardaDia(diaEditado.getDiaDeLaSemana(), idRutinaCompleta, diaEditado);
    }

    public void mostrarBorrarDia(DiaEnRutinaDTO diaEnRutinaDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);
        Button botonBorrar = view.findViewById(R.id.botonConfirmarBorrar);

        botonBorrar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.BotonesImagenesRutinas));

        titulo.setText("Borrar Dia");
        pregunta.setText("¿Deseas borrar el dia " + diaEnRutinaDTO.getNombre() + "?");

        Button cancelar = view.findViewById(R.id.botonCancelarBorrar);
        cancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setOnClickListener(v -> {
            borrarDiaConfirmado(diaEnRutinaDTO);
            dialog.dismiss();
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(0));
        }

        dialog.show();
    }

    private void borrarDiaConfirmado(DiaEnRutinaDTO diaEnRutinaDTO) {

        Long idRutinaCompleta = rutinaActual.getIdRutinaCompleta();

        diasEnRutinaViewModel.borrarDia(diaEnRutinaDTO.getDiaDeLaSemana(), idRutinaCompleta);
    }

    private void borrarRutinaConfirmado(RutinaCompletaDTO rutinaCompletaDTO) {
        rutinasViewModel.borrarRutina(rutinaCompletaDTO.getIdRutinaCompleta(), idUsuario);
    }

    private void mostrarPopUpBorrarEjercicio(EjercicioDTO ejercicioDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.borrar_alimento, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        TextView pregunta = view.findViewById(R.id.preguntaBorrar);
        TextView titulo = view.findViewById(R.id.tituloBorrarAlimento);

        Button confirmar = view.findViewById(R.id.botonConfirmarBorrar);
        confirmar.setBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.fondoContenedorRutinas)
        );

        titulo.setText("Borrar Ejercicio");
        pregunta.setText("¿Deseas borrar " + ejercicioDTO.getNombre() + "?");

        view.findViewById(R.id.botonCancelarBorrar)
                .setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.botonConfirmarBorrar)
                .setOnClickListener(v -> {
                    borrarEjercicioConfirmado(ejercicioDTO);
                    dialog.dismiss();
                });

        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void borrarEjercicioConfirmado(EjercicioDTO ejercicioDTO) {
        ejerciciosViewModel.borrarEjercicio(rutinaActual.getIdRutinaCompleta(), diaActual.getDiaDeLaSemana(), ejercicioDTO.getIdEjercicio());
    }

    private void mostrarPopUpSeries(EjercicioDTO ejercicioDTO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.mostrar_series, null);
        builder.setView(view);

        ejercicioActual = ejercicioDTO;

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        RecyclerView recyclerSeries = view.findViewById(R.id.recyclerSeries);
        Button botonAnadirSerie = view.findViewById(R.id.botonAnadirSerie);

        recyclerSeries.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSeries.setAdapter(seriesAdapter);

        seriesAdapter.setLista(new ArrayList<>());
        serieViewModel.obtenerSeries(ejercicioDTO.getIdEjercicioEnDiaRutina());

        seriesAdapter.setListener(new SeriesAdapter.OnSerieClickListener() {
            @Override
            public void onEditar(SerieDTO serieDTO) {
                serieViewModel.actualizarSerie(serieDTO.getIdSerie(), ejercicioActual.getIdEjercicioEnDiaRutina(), serieDTO);
            }

            @Override
            public void onBorrar(SerieDTO serieDTO) {
                serieViewModel.borrarSerie(serieDTO.getIdSerie(), ejercicioActual.getIdEjercicioEnDiaRutina());
            }
        });

        botonAnadirSerie.setOnClickListener(v -> {
            SerieDTO nuevaSerie = new SerieDTO();
            nuevaSerie.setPeso(0);
            nuevaSerie.setRepeticiones(0);
            nuevaSerie.setSerieAnterior(null);

            serieViewModel.crearSerie(ejercicioDTO.getIdEjercicioEnDiaRutina(), nuevaSerie);
        });

        dialog.show();
    }

    private void cargarRutinas() {
        rutinasViewModel.obtenerLasRutinas(idUsuario);
    }

    private void cargarDias(Long idRutinaCompleta) {
        rutinasViewModel.obtenerRutina(idUsuario, idRutinaCompleta);
    }

    private void cargarEjercicios(Long idRutina, DiaDeLaSemana diaDeLaSemana) {
        ejerciciosViewModel.obtenerLosEjercicios(idRutina, diaDeLaSemana);
    }

    private void observarRutinas() {
        rutinasViewModel.getRutinas().observe(getViewLifecycleOwner(), lista-> {
            rutinasAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        rutinasViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        rutinasViewModel.getRutinaBorrada().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {

                rutinaActual = null;
                diaActual = null;
                posicionAnterior = -1;

                recyclerViewDias.setVisibility(View.GONE);
                recyclerViewEjercicios.setVisibility(View.GONE);

                nombreDia.setVisibility(View.GONE);
                botonEditarNombreDia.setVisibility(View.GONE);
                botonBorrarDia.setVisibility(View.GONE);

                diasRutinaAdapter.setLista(new ArrayList<>());
                ejerciciosRutinaAdapter.setEjercicios(new ArrayList<>());

                Toast.makeText(getContext(), "Rutina eliminada correctamente", Toast.LENGTH_SHORT).show();

                // recargar rutinas
                rutinasViewModel.obtenerLasRutinas(idUsuario);
            }
        });

        rutinasViewModel.getRutinaSeleccionada().observe(getViewLifecycleOwner(), rutina -> {
            if (rutina != null) {
                rutinaActual = rutina;

                List<DiaEnRutinaDTO> diasOrdenados = new ArrayList<>(rutina.getDias());
                Collections.sort(diasOrdenados, (d1, d2) ->
                        d1.getDiaDeLaSemana().ordinal() - d2.getDiaDeLaSemana().ordinal()
                );
                diasRutinaAdapter.setLista(diasOrdenados);
                recyclerViewDias.setVisibility(View.VISIBLE);

                boolean encontrado = false;

                // este if para que se muestre los cambios al editar un dia al instante
                if (diaActual != null) {

                    for (int i = 0; i < diasOrdenados.size(); i++) {
                        if (diasOrdenados.get(i).getIdDiaEnRutina()
                                .equals(diaActual.getIdDiaEnRutina())) {

                            diasRutinaAdapter.setPosicionActual(i);
                            diaActual = diasOrdenados.get(i);
                            mostrarDetalleDia(diaActual);
                            encontrado = true;
                            break;
                        }
                    }
                }

                // este if para cuando borramos un dia y se seleccione otro en vez de quedarse con el dia borrado seleccionado
                if (!encontrado) {

                    if (diasOrdenados.isEmpty()) {

                        diaActual = null;

                        nombreDia.setVisibility(View.GONE);
                        botonEditarNombreDia.setVisibility(View.GONE);
                        botonBorrarDia.setVisibility(View.GONE);
                        recyclerViewEjercicios.setVisibility(View.GONE);

                        ejerciciosRutinaAdapter.setEjercicios(new ArrayList<>());
                    }
                }
            }
        });
    }

    private void observarDias() {
        diasEnRutinaViewModel.getDias().observe(getViewLifecycleOwner(), lista -> {
            diasRutinaAdapter.setLista(lista != null ? lista : new ArrayList<>());
            // con este metodo  al borrar un dia se seleciona el dia anterior.
            if (lista != null && !lista.isEmpty()) {
                int nuevaPosicion = Math.min(diasRutinaAdapter.getPosicionActual(), lista.size() - 1);

                if (nuevaPosicion < 0 || nuevaPosicion >= lista.size()) {
                    nuevaPosicion = 0;
                }

                diasRutinaAdapter.setPosicionActual(nuevaPosicion);

                DiaEnRutinaDTO diaSeleccionado = lista.get(nuevaPosicion);
                mostrarDetalleDia(diaSeleccionado);

            } else {
                // No quedan días
                diasRutinaAdapter.setPosicionActual(-1);
                diaActual = null;

                nombreDia.setVisibility(View.GONE);
                botonEditarNombreDia.setVisibility(View.GONE);
                botonBorrarDia.setVisibility(View.GONE);
                recyclerViewEjercicios.setVisibility(View.GONE);
            }
        });

        diasEnRutinaViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        diasEnRutinaViewModel.getDiaCreadoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día creado correctamente", Toast.LENGTH_SHORT).show();
                if (rutinaActual != null) {
                    rutinasViewModel.obtenerRutina(idUsuario, rutinaActual.getIdRutinaCompleta());
                }
                dialogoActual.dismiss();
            }
        });

        diasEnRutinaViewModel.getDiaActualizadoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día actualizado correctamente", Toast.LENGTH_SHORT).show();
                if (rutinaActual != null) {
                    rutinasViewModel.obtenerRutina(idUsuario, rutinaActual.getIdRutinaCompleta());
                }
                dialogoActual.dismiss();
            }
        });

        diasEnRutinaViewModel.getDiaBorradoExito().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Día eliminado correctamente", Toast.LENGTH_SHORT).show();

                //Limpiar día seleccionado
                diaActual = null;

                //Ocultar detalles del día
                nombreDia.setVisibility(View.GONE);
                botonEditarNombreDia.setVisibility(View.GONE);
                botonBorrarDia.setVisibility(View.GONE);
                recyclerViewEjercicios.setVisibility(View.GONE);

                //Limpiar lista de ejercicios
                ejerciciosRutinaAdapter.setEjercicios(new ArrayList<>());

                //Reset selección del adapter
                diasRutinaAdapter.setPosicionActual(-1);

                //Recargar rutina actual
                if (rutinaActual != null) {
                    rutinasViewModel.obtenerRutina(idUsuario, rutinaActual.getIdRutinaCompleta());
                }
            }
        });
    }

    private void observarEjercicios() {
        ejerciciosViewModel.getEjerciciosRutina().observe(getViewLifecycleOwner(), lista -> {
            ejerciciosRutinaAdapter.setEjercicios(lista != null ? lista : new ArrayList<>());
        });

        ejerciciosViewModel.ejerciciosGlobales().observe(getViewLifecycleOwner(), listaApi -> {
            ejerciciosRutinaAdapter.setApiEjercicios(listaApi);
        });

        ejerciciosViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

        ejerciciosViewModel.getEjercicioBorrado().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null && exito) {
                Toast.makeText(getContext(), "Ejercicio eliminado correctamente", Toast.LENGTH_SHORT).show();
                cargarRutinas();

                if (rutinaActual != null) {
                    cargarDias(rutinaActual.getIdRutinaCompleta());
                }

                if (diaActual != null && rutinaActual != null) {
                    cargarEjercicios(rutinaActual.getIdRutinaCompleta(), diaActual.getDiaDeLaSemana());
                }
            }
        });
    }

    private void observarSeries() {
        serieViewModel.getSeries().observe(getViewLifecycleOwner(), lista -> {
            seriesAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        serieViewModel.getSerieCreada().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null) {
                Toast.makeText(getContext(), "Serie creada correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        serieViewModel.getSerieActualizada().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null) {
                Toast.makeText(getContext(), "Serie actualizada correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        serieViewModel.getSerieBorrada().observe(getViewLifecycleOwner(), exito -> {
            if (exito != null) {
                Toast.makeText(getContext(), "Serie borrada correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        serieViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

    }
}