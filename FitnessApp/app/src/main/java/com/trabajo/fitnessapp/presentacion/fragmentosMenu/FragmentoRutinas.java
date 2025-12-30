package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.presentacion.menu.Dietas.MenuAlimentosActivity;
import com.trabajo.fitnessapp.presentacion.menu.Rutinas.MenuEjercicios;

public class FragmentoRutinas extends Fragment {

    private Long idUsuario;
    private Button botonInformacionEjercicios;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getLong("ID_USUARIO", -1L);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_rutinas, container, false);

        enlazarVistas(view);




        configurarBotones();

        return view;
    }

    private void enlazarVistas(View view) {
        botonInformacionEjercicios = view.findViewById(R.id.botonInformacionEjercicios);
    }

    private void configurarBotones() {
        botonInformacionEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MenuEjercicios.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });
    }
}