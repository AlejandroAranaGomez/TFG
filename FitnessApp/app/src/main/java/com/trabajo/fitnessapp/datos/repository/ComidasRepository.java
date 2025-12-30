package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.api.ComidaService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.ComidaDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComidasRepository {

    private final ComidaService comidaService;

    public ComidasRepository() {
        this.comidaService = RetrofitClient.getClient().create(ComidaService.class);
    }

    public LiveData<Result<List<ComidaDTO>>> obtenerComidas(Long idDiaEnDieta) {
        MutableLiveData<Result<List<ComidaDTO>>> comidas = new MutableLiveData<>();

        comidaService.obtenerComidas(idDiaEnDieta).enqueue(new Callback<List<ComidaDTO>>() {

            @Override
            public void onResponse(Call<List<ComidaDTO>> call, Response<List<ComidaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comidas.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al cargar las comidas";
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
            public void onFailure(Call<List<ComidaDTO>> call, Throwable t) {
                comidas.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return comidas;
    }

    public LiveData<Result<ComidaDTO>> crearComida(Long idDiaEnDieta, ComidaDTO comidaDTO) {
        MutableLiveData<Result<ComidaDTO>> resultado = new MutableLiveData<>();

        comidaService.crearComida(idDiaEnDieta, comidaDTO).enqueue(new Callback<ComidaDTO>() {

            @Override
            public void onResponse(Call<ComidaDTO> call, Response<ComidaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear la comida";
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
            public void onFailure(Call<ComidaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<ComidaDTO>> editarComida(Long idComida, Long idDiaEnDieta, ComidaDTO comidaDTO) {
        MutableLiveData<Result<ComidaDTO>> resultado = new MutableLiveData<>();

        comidaService.editarComida(idComida, idDiaEnDieta, comidaDTO).enqueue(new Callback<ComidaDTO>() {


            @Override
            public void onResponse(Call<ComidaDTO> call, Response<ComidaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al edtar el dia";
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
            public void onFailure(Call<ComidaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarComida(Long idComida, Long idDiaEnDieta) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        comidaService.borrarComida(idComida, idDiaEnDieta).enqueue(new Callback<Void>() {
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
