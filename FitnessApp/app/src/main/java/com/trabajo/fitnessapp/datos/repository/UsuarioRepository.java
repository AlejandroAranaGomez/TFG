package com.trabajo.fitnessapp.datos.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.api.UsuarioService;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioRepository {
    private final UsuarioService usuarioService;

    public UsuarioRepository() {
        this.usuarioService = RetrofitClient.getClient().create(UsuarioService.class);
    }

    public LiveData<Result<UsuarioPerfilDTO>> obtenerPerfil(Long idUsuario) {
        MutableLiveData<Result<UsuarioPerfilDTO>> resultado = new MutableLiveData<>();

        usuarioService.obtenerPerfil(idUsuario).enqueue(new Callback<UsuarioPerfilDTO>() {
            @Override
            public void onResponse(Call<UsuarioPerfilDTO> call, Response<UsuarioPerfilDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error en la base de datos.";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<UsuarioPerfilDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Fallo de conexion con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }
}
