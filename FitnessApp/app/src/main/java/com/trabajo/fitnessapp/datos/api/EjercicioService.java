package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EjercicioService {

    @GET("api/ejercicios/dias/{idDiaEnRutina}")
    Call<List<EjercicioDTO>> obtenerEjercicios(@Path("idDiaEnRutina") Long idDiaEnRutina);

   @POST("api/ejercicios/dias/{idDiaEnRutina}")
   Call<Void> anhadirEjercicio(@Path("idDiaEnRutina") Long idDiaEnRutina, @Body EjercicioDTO ejercicioDTO);

   @DELETE("api/ejercicios/{idEjercicio}/dias/{idDiaEnRutina}")
    Call<Void> borrarEjercicio(@Path("idEjercicio") Long idEjercicio, @Path("idDiaEnRutina") Long idDiaEnRutina);

}
