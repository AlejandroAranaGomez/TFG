package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;
import com.trabajo.fitnessapp.datos.dto.RespuestaEjerciciosDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiEjerciciosService {

    @GET("exercises")
    Call<RespuestaEjerciciosDTO> obtenerEjercicios(
            @Query("page") int page,
            @Query("limit") int limit
    );
}