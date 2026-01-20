package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.api.RutinasService;
import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutinasRepository {

    private final RutinasService rutinasService;

    public RutinasRepository() {
        this.rutinasService = RetrofitClient.getClient().create(RutinasService.class);
    }

    public LiveData<Result<List<RutinaCompletaDTO>>> obtenerRutinasUsuario(Long idUsuario) {
        MutableLiveData<Result<List<RutinaCompletaDTO>>> rutinas = new MutableLiveData<>();

        rutinasService.obtenerRutinaUsuario(idUsuario).enqueue(new Callback<List<RutinaCompletaDTO>>() {
            @Override
            public void onResponse(Call<List<RutinaCompletaDTO>> call, Response<List<RutinaCompletaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rutinas.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al obtener las rutinas";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    rutinas.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<RutinaCompletaDTO>> call, Throwable t) {
                rutinas.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return rutinas;
    }

    public LiveData<Result<RutinaCompletaDTO>> crearRutina(Long idUsuario, RutinaCompletaDTO rutinaCompletaDTO) {
        MutableLiveData<Result<RutinaCompletaDTO>> resultado = new MutableLiveData<>();

        rutinasService.crearRutina(idUsuario, rutinaCompletaDTO).enqueue(new Callback<RutinaCompletaDTO>() {
            @Override
            public void onResponse(Call<RutinaCompletaDTO> call, Response<RutinaCompletaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear la rutina";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<RutinaCompletaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return  resultado;
    }

    public LiveData<Result<RutinaCompletaDTO>> actualizarRutina(Long idUsuario, Long idRutinaCompleta, RutinaCompletaDTO rutinaCompletaDTO) {
        MutableLiveData<Result<RutinaCompletaDTO>> resultado = new MutableLiveData<>();

        rutinasService.editarRutina(idUsuario, idRutinaCompleta, rutinaCompletaDTO).enqueue(new Callback<RutinaCompletaDTO>() {
            @Override
            public void onResponse(Call<RutinaCompletaDTO> call, Response<RutinaCompletaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al actualizar la rutina";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<RutinaCompletaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarRutina(Long idUsuario, Long idRutinaCompleta) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        rutinasService.borrarRutina(idUsuario, idRutinaCompleta).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar la rutina";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
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
