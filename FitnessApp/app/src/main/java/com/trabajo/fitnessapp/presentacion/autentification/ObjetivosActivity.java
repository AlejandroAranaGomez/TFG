package com.trabajo.fitnessapp.presentacion.autentification;

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
import com.trabajo.fitnessapp.dominio.Objetivos;
import com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity;


public class ObjetivosActivity extends AppCompatActivity {

    private Button botonSiguiente;
    private ImageButton botonVolverBienvenda;
    private RadioGroup radioGroup;
    private RadioButton perderGrasa, ganarMusculo, mantenerPeso;

    private ProgressBar barraProgresoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_objetivos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        enlazarVistas();

       configurarBotones();

        barraProgresoActividad.setProgress(33);

    }

    private void enlazarVistas() {
        botonSiguiente = findViewById(R.id.botonSiguiente);
        botonVolverBienvenda = findViewById(R.id.botonVolverBienvenida);

        radioGroup = findViewById(R.id.grupoObjetivos);
        perderGrasa = findViewById(R.id.perderGrasa);
        ganarMusculo = findViewById(R.id.ganarMusculo);
        mantenerPeso = findViewById(R.id.mantenerPeso);

        View barraProgreso = findViewById(R.id.barraProgreso);
        barraProgresoActividad = barraProgreso.findViewById(R.id.barraProgresoLayout);

    }
    private void configurarBotones() {

        botonSiguiente.setOnClickListener(v -> {
            selecionarYContinuar();
        });

        botonVolverBienvenda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void selecionarYContinuar() {

        int opcionElegida = radioGroup.getCheckedRadioButtonId();

        Objetivos objetivoSeleccionado = null;

        if (opcionElegida == R.id.perderGrasa) {
            objetivoSeleccionado = Objetivos.PERDER_GRASA;
        } else if (opcionElegida == R.id.ganarMusculo) {
            objetivoSeleccionado = Objetivos.GANAR_MASA_MUSCULAR;
        } else if (opcionElegida == R.id.mantenerPeso) {
            objetivoSeleccionado = Objetivos.MANTENER_PESO;
        }

        if (objetivoSeleccionado == null) {
            Toast.makeText(this, "Por favor, seleccione un objetivo.", Toast.LENGTH_LONG).show();
        } else {
            siguientePaso(objetivoSeleccionado);
        }
    }

    private void siguientePaso(Objetivos objetivo) {
        Intent intent = new Intent(ObjetivosActivity.this, NivelActividadActivity.class);
        intent.putExtra("Objetivo_seleccionado", objetivo.name());
        startActivity(intent);
    }
}