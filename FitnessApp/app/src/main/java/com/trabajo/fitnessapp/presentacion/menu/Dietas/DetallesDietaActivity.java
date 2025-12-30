package com.trabajo.fitnessapp.presentacion.menu.Dietas;

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
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;

public class DetallesDietaActivity extends AppCompatActivity {

    private DietasViewModel viewModel;
    private ImageButton botonVolver;
    private Button botonGuardar;
    private EditText editNombre, editDescripcion;
    private TextView tituloPantalla;
    private Long idUsuario;
    private DietaCompletaDTO dietaCompletaDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_dieta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1L);
        dietaCompletaDTO = (DietaCompletaDTO) getIntent().getSerializableExtra("DIETA");

        enlazarVistas();
        viewModel = new ViewModelProvider(this).get(DietasViewModel.class);
        configurarPantalla();
        configurarBotones();
        observarDatos();
    }

    private void enlazarVistas() {
        botonVolver = findViewById(R.id.botonVolver);
        editNombre = findViewById(R.id.editNombreDieta);
        editDescripcion = findViewById(R.id.editDescripcionDieta);
        tituloPantalla = findViewById(R.id.tituloDetallesDietas);
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
        if (dietaCompletaDTO != null) {
            tituloPantalla.setText("Editar Dieta");
            botonGuardar.setText("Actualizar Cambios");

            editNombre.setText(dietaCompletaDTO.getNombre());
            editDescripcion.setText(dietaCompletaDTO.getDescripcion());
        } else {
            tituloPantalla.setText("Nueva Dieta");
            botonGuardar.setText("Crear Dieta");
        }
    }

    private void guardarCambios() {
        String nombre = editNombre.getText().toString().trim();
        String descripcion = editDescripcion.getText().toString().trim();


        if (dietaCompletaDTO != null) {
            dietaCompletaDTO.setNombre(nombre);
            dietaCompletaDTO.setDescripcion(descripcion);

            viewModel.actualizarDieta(dietaCompletaDTO.getIdDietaCompleta(), idUsuario, dietaCompletaDTO);

        } else {
            DietaCompletaDTO nuevaDieta = new DietaCompletaDTO();
            nuevaDieta.setNombre(nombre);
            nuevaDieta.setDescripcion(descripcion);
            nuevaDieta.setActiva(false);

            nuevaDieta.setCaloriasTotales(0);
            nuevaDieta.setCarbohidratos(0);
            nuevaDieta.setProteinas(0);
            nuevaDieta.setGrasas(0);

            viewModel.crearDieta(idUsuario, nuevaDieta);
        }
    }

    private void observarDatos() {
        viewModel.getDietaCreada().observe(this, dieta -> {
            if (dieta != null) {
                Toast.makeText(this, "Dieta creada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getDietaActualizada().observe(this, dieta -> {
            if (dieta != null) {
                Toast.makeText(this, "Dieta actualizada", Toast.LENGTH_SHORT).show();
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