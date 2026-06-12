package com.trabajo.fitnessapp.presentacion.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivo;

public class NivelActividadActivity extends AppCompatActivity {


    private Button botonSiguiente;
    private ImageButton botonVolverObjetivo;
    private RadioGroup radioGroup;
    private RadioButton botonSedentario, botonEjercicioLigero, botonEjercicioModerado, botonEjercicioIntenso, botonAtleta;
    private Objetivo objetivoSeleccionado;
    private ProgressBar barraProgresoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nivel_actividad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            String objetivo = getIntent().getStringExtra("Objetivo_seleccionado");
            objetivoSeleccionado = Objetivo.valueOf(objetivo);
        } catch (Exception e) {
            Toast.makeText(this, "Error al recibir el objetivo.", Toast.LENGTH_LONG).show();
        }

        enlazarVistas();

        configuracionBotones();

        barraProgresoActividad.setProgress(66);

    }

    private void enlazarVistas() {
        botonSiguiente = findViewById(R.id.botonSiguiente);
        botonVolverObjetivo = findViewById(R.id.botonVolverObjetivo);

        radioGroup = findViewById(R.id.grupoActividad);

        botonSedentario = findViewById(R.id.botonSedentario);
        botonEjercicioLigero = findViewById(R.id.botonEjercicioLigero);
        botonEjercicioModerado = findViewById(R.id.botonEjercicioModerado);
        botonEjercicioIntenso = findViewById(R.id.botonEjercicioIntenso);
        botonAtleta = findViewById(R.id.botonAtleta);

        View barraProgreso = findViewById(R.id.barraProgreso);
        barraProgresoActividad = barraProgreso.findViewById(R.id.barraProgresoLayout);
    }

    private void configuracionBotones() {
        botonSiguiente.setOnClickListener(v -> {
            seleccionarYContinuar();
        });

        botonVolverObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void seleccionarYContinuar() {
        int opcionElegida = radioGroup.getCheckedRadioButtonId();

        NivelDeActividad actividadSeleccionada = null;

        if (opcionElegida == R.id.botonSedentario) {
            actividadSeleccionada = NivelDeActividad.SEDENTARIO;
        } else if (opcionElegida == R.id.botonEjercicioLigero) {
            actividadSeleccionada = NivelDeActividad.EJERCICIO_LIGERO;
        } else if (opcionElegida == R.id.botonEjercicioModerado) {
            actividadSeleccionada = NivelDeActividad.EJERCICIO_MODERADO;
        } else if (opcionElegida == R.id.botonEjercicioIntenso) {
            actividadSeleccionada = NivelDeActividad.EJERCICIO_INTENSO;
        } else if (opcionElegida == R.id.botonAtleta) {
            actividadSeleccionada = NivelDeActividad.ATLETA;
        }

        if (actividadSeleccionada == null) {
            Toast.makeText(this, "Por favor, seleccione un nivel de actividad.", Toast.LENGTH_LONG).show();
        } else {
            siguientePaso(actividadSeleccionada);
        }
    }

    private void siguientePaso(NivelDeActividad actividadSeleccionada) {
        Intent intent = new Intent(NivelActividadActivity.this, RegisterActivity.class);
        intent.putExtra("Objetivo_seleccionado", objetivoSeleccionado.name());
        intent.putExtra("Actividad_seleccionada", actividadSeleccionada.name());
        startActivity(intent);
    }
}