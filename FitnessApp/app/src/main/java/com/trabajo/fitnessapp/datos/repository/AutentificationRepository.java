package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.api.AutorizacionService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.InicioSesionDTO;
import com.trabajo.fitnessapp.datos.dto.RegistroDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Esta clase hace las llamadas a la api relativas al registro.
public class AutentificationRepository {

    private final AutorizacionService autorizacionService;

    public AutentificationRepository() {
        this.autorizacionService = RetrofitClient.getClient().create(AutorizacionService.class);
    }

    public LiveData<Result<UsuarioDTO>> registrarUsuario(RegistroDTO registroDTO) {
        MutableLiveData<Result<UsuarioDTO>> resultadoConsulta = new MutableLiveData<>();

        autorizacionService.registrarUsuario(registroDTO).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultadoConsulta.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error en la api.";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultadoConsulta.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                resultadoConsulta.setValue(new Result.Error<>("Fallo de conexion con la api: " + t.getMessage()));
            }
        });

        return resultadoConsulta;
    }

    public LiveData<Result<UsuarioDTO>> iniciarSesion(InicioSesionDTO inicioSesionDTO) {
        MutableLiveData<Result<UsuarioDTO>> resultadoConsulta = new MutableLiveData<>();

        autorizacionService.iniciarSesion(inicioSesionDTO).enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultadoConsulta.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error en las credenciales.";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultadoConsulta.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                resultadoConsulta.setValue(new Result.Error<>("Fallo de conexion con la api: " + t.getMessage()));
            }
        });
        return resultadoConsulta;
    }
}
