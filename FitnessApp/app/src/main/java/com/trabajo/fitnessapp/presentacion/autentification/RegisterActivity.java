package com.trabajo.fitnessapp.presentacion.autentification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast; // Para mostrar mensajes al usuario

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.dominio.Generos;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivos;
import com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel viewModel;

    // Campos de la interfaz
    private EditText editNombre, editApellido1, editApellido2,  editEmail, editContrasenha,
            editTelefono, editFechaNacimiento, editPeso, editAltura;

    private Spinner spinnerGenero;
    private Button botonRegistrar;
    private ImageButton botonVolverActividad;
    private Objetivos objetivoSeleccionado;
    private NivelDeActividad nivelDeActividadSeleccionado;
    private ProgressBar barraProgresoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRegister), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        try {
            String objetivo = getIntent().getStringExtra("Objetivo_seleccionado");
            objetivoSeleccionado = Objetivos.valueOf(objetivo);
            String nivelDeActividad = getIntent().getStringExtra("Actividad_seleccionada");
            nivelDeActividadSeleccionado = NivelDeActividad.valueOf(nivelDeActividad);
        } catch (Exception e) {
            Toast.makeText(this, "Error al recibir los datos.", Toast.LENGTH_LONG).show();
        }

        enlazarVistas();

        configurarBotones();

        mensajeAlRegistrar();

        barraProgresoActividad.setProgress(100);

    }

    /**
     * Enlaza las variables de Java con los IDs del archivo XML
     */
    private void enlazarVistas() {
        editNombre = findViewById(R.id.editNombre);
        editApellido1 = findViewById(R.id.editApellido1);
        editApellido2 = findViewById(R.id.editApellido2);
        editEmail = findViewById(R.id.editEmail);
        editContrasenha = findViewById(R.id.editContrasenha);
        editTelefono = findViewById(R.id.editTelefono);
        editFechaNacimiento = findViewById(R.id.editFechaNacimiento);

        spinnerGenero = findViewById(R.id.spinnerGenero);

        List<String> opcinesGenero = new ArrayList<>();
        opcinesGenero.add("Genero");
        for (Generos generos : Generos.values()) {
            opcinesGenero.add(generos.name());
        }
        ArrayAdapter<String> adapter = editDesplegableGenero(opcinesGenero);
        spinnerGenero.setAdapter(adapter);

        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        botonRegistrar = findViewById(R.id.botonRegistrar);
        botonVolverActividad = findViewById(R.id.botonVolverActividad);

        View barraProgreso = findViewById(R.id.barraProgreso);
        barraProgresoActividad = barraProgreso.findViewById(R.id.barraProgresoLayout);
    }

    private void configurarBotones() {
        // Boton para volver atras
        botonVolverActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonRegistrar.setOnClickListener(v -> {
            registrarUsuarioDesdeFormulario();
        });
    }

    private ArrayAdapter<String> editDesplegableGenero(List<String> opcinesGenero) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcinesGenero) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#757474"));
                }else {
                    tv.setTextColor(Color.parseColor("#FFFFFF"));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                view.setBackgroundColor(Color.parseColor("#1E1E1E"));
                tv.setTextColor(Color.WHITE);

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void registrarUsuarioDesdeFormulario() {

        viewModel.registrar(editNombre.getText().toString().trim(),
                editApellido1.getText().toString().trim(),
                editApellido2.getText().toString().trim(),
                editEmail.getText().toString().trim(),
                editContrasenha.getText().toString().trim(),
                editTelefono.getText().toString().trim(),
                editFechaNacimiento.getText().toString().trim(),
                (String) spinnerGenero.getSelectedItem(),
                editPeso.getText().toString().trim(),
                editAltura.getText().toString().trim(),
                objetivoSeleccionado,
                nivelDeActividadSeleccionado
                );
    }

    // Mensaje que se muestra al registrarse dependiendo de los datos que escribas.
    private void mensajeAlRegistrar() {
        viewModel.getMensajeError().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });

        viewModel.getRegistrarExito().observe(this, mensaje -> {
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        });
    }
}