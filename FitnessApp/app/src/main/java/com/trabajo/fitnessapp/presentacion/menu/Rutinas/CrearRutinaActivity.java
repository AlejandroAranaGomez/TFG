package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

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

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;

public class CrearRutinaActivity extends AppCompatActivity {

    private RutinasViewModel viewModel;
    private ImageButton botonVolver;
    private Button botonGuardar;
    private EditText editNombre, editResumen;
    private TextView tituloPantalla;
    private Long idUsuario;
    private RutinaCompletaDTO rutinaCompletaDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_rutina);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1L);
        rutinaCompletaDTO = (RutinaCompletaDTO) getIntent().getSerializableExtra("RUTINA");

        enlazarVistas();
        viewModel = new ViewModelProvider(this).get(RutinasViewModel.class);
        configurarPantalla();
        configurarBotones();
        observarDatos();
    }

    private void enlazarVistas() {
        botonVolver = findViewById(R.id.botonVolver);
        editNombre = findViewById(R.id.editNombreRutina);
        editResumen = findViewById(R.id.editResumenRutina);
        tituloPantalla = findViewById(R.id.tituloDetallesRutinas);
        botonGuardar = findViewById(R.id.botonGuardar);
    }

    private void configurarBotones() {
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void configurarPantalla() {
        if (rutinaCompletaDTO != null) {
            tituloPantalla.setText("Editar Rutina");
            botonGuardar.setText("Actualizar Cambios");

            editNombre.setText(rutinaCompletaDTO.getNombreRutinaCompleta());
            editResumen.setText(rutinaCompletaDTO.getResumen());
        } else {
            tituloPantalla.setText("Nueva Rutina");
            botonGuardar.setText("Crear Rutina");
        }
    }

    private void guardarCambios() {
        String nombre = editNombre.getText().toString().trim();
        String resumen = editResumen.getText().toString().trim();

        if (rutinaCompletaDTO != null) {
            rutinaCompletaDTO.setNombreRutinaCompleta(nombre);
            rutinaCompletaDTO.setResumen(resumen);

            viewModel.actualizarRutina(rutinaCompletaDTO.getIdRutinaCompleta(), idUsuario, rutinaCompletaDTO);
        } else {
            RutinaCompletaDTO nuevaRutina = new RutinaCompletaDTO();
            nuevaRutina.setNombreRutinaCompleta(nombre);
            nuevaRutina.setResumen(resumen);

            viewModel.crearRutina(idUsuario, nuevaRutina);
        }
    }

    private void observarDatos() {
        viewModel.getRutinaCreada().observe(this, rutina -> {
            if (rutina != null) {
                Toast.makeText(this, "Rutina creada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getRutinaActualizada().observe(this, rutina -> {
            if (rutina != null) {
                Toast.makeText(this, "Rutina actualizada", Toast.LENGTH_SHORT).show();
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