package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.api.AlimentosService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlimentosRepository {

    private final AlimentosService alimentosService;

    public AlimentosRepository() {
        this.alimentosService = RetrofitClient.getClient().create(AlimentosService.class);
    }

    public LiveData<Result<List<AlimentoDTO>>> obtenerAlimentosUsuario(Long idUsuario) {
        MutableLiveData<Result<List<AlimentoDTO>>> listaAlimentos = new MutableLiveData<>();

        alimentosService.obtenerAlimentosUsuario(idUsuario).enqueue(new Callback<List<AlimentoDTO>>() {
            @Override
            public void onResponse(Call<List<AlimentoDTO>> call, Response<List<AlimentoDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlimentos.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error en la base de datos.";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    listaAlimentos.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<AlimentoDTO>> call, Throwable t) {
                listaAlimentos.setValue(new Result.Error<>("Fallo de conexion con la base de datos: " + t.getMessage()));
            }
        });
        return listaAlimentos;
    }

    public LiveData<Result<AlimentoDTO>> actualizarAlimento(Long idAlimento, Long idUsuario, AlimentoDTO alimentoDTO) {
        MutableLiveData<Result<AlimentoDTO>> resultado = new MutableLiveData<>();

        alimentosService.actualizarAlimento(idAlimento, idUsuario, alimentoDTO).enqueue(new Callback<AlimentoDTO>() {
            @Override
            public void onResponse(Call<AlimentoDTO> call, Response<AlimentoDTO> response) {
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
            public void onFailure(Call<AlimentoDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Fallo de conexion con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarAlimento(Long idAlimento, Long idUsuario) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        alimentosService.borrarAlimento(idAlimento, idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
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
            public void onFailure(Call<Void> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Fallo de conexion con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<AlimentoDTO>> crearAlimento(Long idUsuario, AlimentoDTO alimentoDTO) {
        MutableLiveData<Result<AlimentoDTO>> resultado = new MutableLiveData<>();

        alimentosService.crearAlimento(idUsuario, alimentoDTO).enqueue(new Callback<AlimentoDTO>() {
            @Override
            public void onResponse(Call<AlimentoDTO> call, Response<AlimentoDTO> response) {
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
            public void onFailure(Call<AlimentoDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Fallo de conexion con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }
}
