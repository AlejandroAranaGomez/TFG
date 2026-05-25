package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiEjerciciosService {
    @GET("api/ejercicios")
    Call<List<ApiEjercicioDTO>> buscarEjercicios();
}