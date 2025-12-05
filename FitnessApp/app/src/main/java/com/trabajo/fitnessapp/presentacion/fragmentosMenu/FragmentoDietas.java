package com.trabajo.fitnessapp.presentacion.fragmentosMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.presentacion.menu.MenuAlimentos;

import java.util.ArrayList;

public class FragmentoDietas extends Fragment {
    private Button botonInformacionAlimentos;
    private Long idUsuario;

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
        View view = inflater.inflate(R.layout.fragmento_dietas, container, false);

        enlazarVistas(view);
        configurarBotones();

        return view;
    }

    private void enlazarVistas(View view) {
        botonInformacionAlimentos = view.findViewById(R.id.botonInformacionAlimentos);
    }

    private void configurarBotones() {
        botonInformacionAlimentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), MenuAlimentos.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
            }
        });
    }

}




