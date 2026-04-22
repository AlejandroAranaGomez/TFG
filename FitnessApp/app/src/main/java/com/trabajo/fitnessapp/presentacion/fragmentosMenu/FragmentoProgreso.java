package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.HistorialPesoDTO;
import com.trabajo.fitnessapp.datos.dto.PesoDTO;
import com.trabajo.fitnessapp.presentacion.adaptador.HistorialPesoAdapter;
import com.trabajo.fitnessapp.presentacion.menu.Progreso.ProgresoViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentoProgreso extends Fragment {

    private Long idUsuario;
    private TextView textoPesoActual;
    private EditText editPeso;
    private Button botonActualizarPeso;
    private RecyclerView recyclerHistorialPeso;
    private LineChart graficaPeso;
    private ProgresoViewModel progresoViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getLong("ID_USUARIO", -1L);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragmento_progreso, container, false);

       enlazarVistas(view);

       progresoViewModel = new ViewModelProvider(this).get(ProgresoViewModel.class);

       configurarBotones();

       observarDatos();

       progresoViewModel.cargarHistorial(idUsuario);

       return view;
    }

    private void enlazarVistas(View view) {

        textoPesoActual = view.findViewById(R.id.textPesoActual);
        botonActualizarPeso = view.findViewById(R.id.botonActualizarPeso);
        editPeso = view.findViewById(R.id.editPeso);
        graficaPeso = view.findViewById(R.id.graficaPeso);
        recyclerHistorialPeso = view.findViewById(R.id.recyclerHistorialPeso);
    }

    private void configurarBotones(){

        botonActualizarPeso.setOnClickListener(v -> {
            String pesoTexto = editPeso.getText().toString().trim();

            if(pesoTexto.isEmpty()){
                return;
            }
            float peso = Float.parseFloat(pesoTexto);
            PesoDTO pesoDTO = new PesoDTO(peso);
            progresoViewModel.actualizarPeso(idUsuario, pesoDTO);
            editPeso.setText("");

        });

    }

    private void mostrarGrafica(List<HistorialPesoDTO> historial) {

        List<Entry> entradas = new ArrayList<>();
        List<String> fechas = new ArrayList<>();

        for(int i = 0; i < historial.size(); i++){

            float peso = historial.get(i).getPeso();
            String fecha = historial.get(i).getFecha();

            entradas.add(new Entry(i, peso));
            fechas.add(fecha);
        }

        LineDataSet dataSet = new LineDataSet(entradas, "Peso");

        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setValueTextSize(10f);

        LineData lineData = new LineData(dataSet);

        graficaPeso.setData(lineData);

        // CONFIGURAR EJE X
        XAxis xAxis = graficaPeso.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        graficaPeso.setExtraLeftOffset(10f);
        graficaPeso.setExtraRightOffset(10f);
        graficaPeso.setExtraBottomOffset(15f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(fechas));

        graficaPeso.invalidate();
    }

    private void mostrarHistorial(List<HistorialPesoDTO> historial) {

        recyclerHistorialPeso.setLayoutManager(new LinearLayoutManager(getContext()));

        HistorialPesoAdapter adapter = new HistorialPesoAdapter(historial);

        recyclerHistorialPeso.setAdapter(adapter);
    }

    private void observarDatos(){

        progresoViewModel.getHistorialPeso().observe(getViewLifecycleOwner(), historial -> {

            if(historial != null && !historial.isEmpty()){

                float pesoActual = historial.get(historial.size() - 1).getPeso();
                textoPesoActual.setText(pesoActual + " kg");

                mostrarGrafica(historial);

                mostrarHistorial(historial);

            } else {

                textoPesoActual.setText("-- kg");

            }

        });

        progresoViewModel.getMensajeError().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
            }
        });

    }
}