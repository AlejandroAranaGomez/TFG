package com.trabajo.fitnessapp.datos.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.api.AlimentosService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlimentosRepository {

    private final AlimentosService alimentosService;

    public AlimentosRepository() {
        this.alimentosService = RetrofitClient.getClient().create(AlimentosService.class);
    }

    public LiveData<List<AlimentoDTO>> obtenerAlimentosUsuario(Long idUsuario) {
        MutableLiveData<List<AlimentoDTO>> listaAlimentos = new MutableLiveData<>();

        alimentosService.obtenerAlimentosUsuario(idUsuario).enqueue(new Callback<List<AlimentoDTO>>() {
            @Override
            public void onResponse(Call<List<AlimentoDTO>> call, Response<List<AlimentoDTO>> response) {
                if (response.isSuccessful()) {
                    listaAlimentos.setValue(response.body());
                } else {
                    listaAlimentos.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<AlimentoDTO>> call, Throwable t) {
                listaAlimentos.setValue(null);
            }
        });
        return listaAlimentos;
    }

    public LiveData<Boolean> actualizarAlimento(Long idAlimento, Long idUsuario, AlimentoDTO alimentoDTO) {
        MutableLiveData<Boolean> resultado = new MutableLiveData<>();

        alimentosService.actualizarAlimento(idAlimento, idUsuario, alimentoDTO).enqueue(new Callback<AlimentoDTO>() {
            @Override
            public void onResponse(Call<AlimentoDTO> call, Response<AlimentoDTO> response) {
                resultado.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<AlimentoDTO> call, Throwable t) {
                resultado.setValue(false);
            }
        });
        return resultado;
    }

    public LiveData<Boolean> borrarAlimento(Long idAlimento, Long idUsuario) {
        MutableLiveData<Boolean> resultado = new MutableLiveData<>();

        alimentosService.borrarAlimento(idAlimento, idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                resultado.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultado.setValue(false);
            }
        });
        return resultado;
    }

    public LiveData<Boolean> crearAlimento(Long idUsuario, AlimentoDTO alimentoDTO) {
        MutableLiveData<Boolean> resultado = new MutableLiveData<>();

        alimentosService.crearAlimento(idUsuario, alimentoDTO).enqueue(new Callback<AlimentoDTO>() {
            @Override
            public void onResponse(Call<AlimentoDTO> call, Response<AlimentoDTO> response) {
                resultado.setValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<AlimentoDTO> call, Throwable t) {
                resultado.setValue(false);
            }
        });
        return resultado;
    }
}
