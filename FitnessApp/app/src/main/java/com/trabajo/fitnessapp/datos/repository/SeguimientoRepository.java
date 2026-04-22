package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.api.SeguimientoService;
import com.trabajo.fitnessapp.datos.dto.ComidaSeguimientoDTO;
import com.trabajo.fitnessapp.datos.dto.RegistroComidaDiariaDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeguimientoRepository {

    private SeguimientoService seguimientoService;

    public SeguimientoRepository() {
        this.seguimientoService = RetrofitClient.getClient().create(SeguimientoService.class);
    }

    public LiveData<Result<List<ComidaSeguimientoDTO>>> obtenerComidasHoy(Long idUsuario) {
        MutableLiveData<Result<List<ComidaSeguimientoDTO>>> comidas = new MutableLiveData<>();

        seguimientoService.obtenerComidasHoy(idUsuario).enqueue(new Callback<List<ComidaSeguimientoDTO>>() {
            @Override
            public void onResponse(Call<List<ComidaSeguimientoDTO>> call, Response<List<ComidaSeguimientoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comidas.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al obtener las comidas de hoy";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    comidas.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<ComidaSeguimientoDTO>> call, Throwable t) {
                comidas.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return comidas;
    }

    public LiveData<Result<RegistroComidaDiariaDTO>> registrarComidaRealizada(Long idUsuario, Long idComida) {
        MutableLiveData<Result<RegistroComidaDiariaDTO>> resultado = new MutableLiveData<>();

        seguimientoService.registrarComidaRealizada(idUsuario, idComida).enqueue(new Callback<RegistroComidaDiariaDTO>() {
            @Override
            public void onResponse(Call<RegistroComidaDiariaDTO> call, Response<RegistroComidaDiariaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al registrar la comida";
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
            public void onFailure(Call<RegistroComidaDiariaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> eliminarComidaRealizada(Long idUsuario, Long idComida) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        seguimientoService.eliminarComidaRealizada(idUsuario, idComida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar la comida";
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
            public void onFailure(Call<Void> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

}
