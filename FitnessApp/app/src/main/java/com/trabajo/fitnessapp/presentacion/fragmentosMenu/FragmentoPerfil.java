package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;
import com.trabajo.fitnessapp.presentacion.menu.Perfil.EditarPerfilActivity;
import com.trabajo.fitnessapp.presentacion.menu.Perfil.PerfilViewModel;

import java.time.LocalDate;
import java.time.Period;

public class FragmentoPerfil extends Fragment {

    private Button botonEditarPerfil;
    private Long idUsuario;
    private PerfilViewModel perfilViewModel;
    private TextView tvNombre, tvEdad, tvPeso, tvAltura, tvFechaDeNacimiento, tvObjetivo, tvActividad, tvEmail, tvTelefono, tvGenero;


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
        View view = inflater.inflate(R.layout.fragmento_perfil, container, false);

        enlazarVistas(view);

        perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        configurarBotones();
        observarViewModel();



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (idUsuario != null && idUsuario != -1L) {
            perfilViewModel.cargarPerfil(idUsuario);
        }
    }

    private void enlazarVistas(View view) {
        tvNombre = view.findViewById(R.id.tvNombre);
        tvEdad = view.findViewById(R.id.tvEdad);
        tvPeso = view.findViewById(R.id.tvPeso);
        tvAltura = view.findViewById(R.id.tvAltura);
        tvFechaDeNacimiento = view.findViewById(R.id.tvFechaNacimiento);
        tvObjetivo = view.findViewById(R.id.tvObjetivo);
        tvActividad = view.findViewById(R.id.tvActividad);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvGenero = view.findViewById(R.id.tvGenero);
        botonEditarPerfil = view.findViewById(R.id.btnEditarPerfil);
    }

    private void configurarBotones() {
        botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), EditarPerfilActivity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });
    }

    private void mostrarDatos(UsuarioPerfilDTO perfil) {

        tvNombre.setText(
                perfil.getNombre() + " " +
                        perfil.getApellido1() + " " +
                        perfil.getApellido2()
        );

        tvEdad.setText(calcularEdad(perfil.getFechaNacimiento()) + " años");

        tvPeso.setText(perfil.getPeso() + " kg");
        tvAltura.setText(perfil.getAltura() + " cm");
        tvFechaDeNacimiento.setText(perfil.getFechaNacimiento());
        tvObjetivo.setText(formatearEnum(perfil.getObjetivo()));
        tvActividad.setText(formatearEnum(perfil.getNivelDeActividad()));
        tvEmail.setText(perfil.getEmail());
        tvTelefono.setText(perfil.getTelefono());
        tvGenero.setText(perfil.getGenero().toString());
    }

    private int calcularEdad(String fechaNacimiento) {
        LocalDate fechaNac = LocalDate.parse(fechaNacimiento);
        return Period.between(fechaNac, LocalDate.now()).getYears();
    }

    private String formatearEnum(Enum<?> valor) {
        if (valor == null) return "No definido";

        String texto = valor.name().toLowerCase().replace("_", " ");
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }


    private void observarViewModel() {

        perfilViewModel.getPerfilExito().observe(getViewLifecycleOwner(), perfil -> {
            if (perfil != null) {
                mostrarDatos(perfil);
            }
        });

        perfilViewModel.getMensajeError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }
}