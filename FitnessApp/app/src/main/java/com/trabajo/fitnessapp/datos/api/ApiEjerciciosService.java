package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ApiEjerciciosService {

    @Headers({
            "X-RapidAPI-Host: exercisedb.p.rapidapi.com"
    })
    @GET("exercises")
    Call<List<ApiEjercicioDTO>> obtenerEjercicios(
            @Header("X-RapidAPI-Key") String apiKey
    );
}


apikey = "4451dfe8a5mshfb27bdef97ae35ep143379jsn27500955e5f1"