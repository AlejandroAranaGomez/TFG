package com.trabajo.fitnessapp.presentacion.menu.Perfil;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.UsuarioActualizarDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivo;

import java.util.ArrayList;
import java.util.List;

public class EditarPerfilActivity extends AppCompatActivity {

    private Long idUsuario;
    private ImageButton botonVolverActividad;
    private Button guardarPerfil;
    private PerfilViewModel perfilViewModel;
    private EditText nombre, apellido1, apellido2, fechaNacimiento, altura, telefono;
    private Spinner objetivo, actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1l);

        enlazarVistas();
        configurarSpinners();

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        observarViewModel();

        if (idUsuario != -1L) {
            perfilViewModel.cargarPerfil(idUsuario);
        }

        configurarBotones();

    }

    private void enlazarVistas() {
        nombre = findViewById(R.id.textNombre);
        apellido1 = findViewById(R.id.textApellido1);
        apellido2 = findViewById(R.id.textApellido2);
        fechaNacimiento = findViewById(R.id.textFechaNacimiento);
        altura = findViewById(R.id.textAltura);
        telefono = findViewById(R.id.textTelefono);
        objetivo = findViewById(R.id.spinnerObjetivo);
        actividad = findViewById(R.id.spinnerActividad);

        botonVolverActividad = findViewById(R.id.botonVolverActividad);
        guardarPerfil = findViewById(R.id.botonGuardarPerfil);
    }

    private void configurarBotones() {
        botonVolverActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        guardarPerfil.setOnClickListener(v -> actualizarPerfil());
    }

    private void mostrarDatos(UsuarioPerfilDTO perfil){

        nombre.setText(perfil.getNombre());
        apellido1.setText(perfil.getApellido1());
        apellido2.setText(perfil.getApellido2());
        fechaNacimiento.setText(perfil.getFechaNacimiento());
        altura.setText(String.valueOf(perfil.getAltura()));
        telefono.setText(perfil.getTelefono());

        seleccionarSpinner(objetivo, formatearEnum(perfil.getObjetivo()));
        seleccionarSpinner(actividad, formatearEnum(perfil.getNivelDeActividad()));

    }

    private ArrayAdapter<String> crearAdapterPersonalizado(List<String> opciones) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;

                tv.setTextColor(Color.WHITE);

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

    private void configurarSpinners() {

        List<String> objetivos = new ArrayList<>();
        for (Objetivo obj : Objetivo.values()) {
            objetivos.add(formatearEnum(obj));
        }

        ArrayAdapter<String> objetivoAdapter = crearAdapterPersonalizado(objetivos);
        objetivo.setAdapter(objetivoAdapter);



        List<String> actividades = new ArrayList<>();
        for (NivelDeActividad act : NivelDeActividad.values()) {
            actividades.add(formatearEnum(act));
        }

        ArrayAdapter<String> actividadAdapter = crearAdapterPersonalizado(actividades);
        actividad.setAdapter(actividadAdapter);
    }

    private void seleccionarSpinner(Spinner spinner, String valor){

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();

        for(int i = 0; i < adapter.getCount(); i++){
            if(adapter.getItem(i).equals(valor)){
                spinner.setSelection(i);
                break;
            }
        }
    }

    private String formatearEnum(Enum<?> valor) {

        String texto = valor.name().toLowerCase().replace("_", " ");
        return texto.substring(0,1).toUpperCase() + texto.substring(1);
    }

    private void actualizarPerfil() {

        UsuarioActualizarDTO dto = new UsuarioActualizarDTO();

        dto.setNombre(nombre.getText().toString());
        dto.setApellido1(apellido1.getText().toString());
        dto.setApellido2(apellido2.getText().toString());
        dto.setFechaNacimiento(fechaNacimiento.getText().toString());
        dto.setTelefono(telefono.getText().toString());

        if(!altura.getText().toString().isEmpty()){
            dto.setAltura(Integer.parseInt(altura.getText().toString()));
        }

        dto.setObjetivo(Objetivo.valueOf(
                ((String) objetivo.getSelectedItem()).toUpperCase().replace(" ", "_")
        ));

        dto.setNivelDeActividad(NivelDeActividad.valueOf(
                ((String) actividad.getSelectedItem()).toUpperCase().replace(" ", "_")
        ));

        perfilViewModel.editarPerfil(idUsuario, dto);
    }

    private void observarViewModel() {

        perfilViewModel.getPerfilExito().observe(this, perfil -> {
            if (perfil != null) {
                mostrarDatos(perfil);
            }
        });

        perfilViewModel.getPerfilActualizado().observe(this, perfil -> {
            if (perfil != null) {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        perfilViewModel.getMensajeError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

}