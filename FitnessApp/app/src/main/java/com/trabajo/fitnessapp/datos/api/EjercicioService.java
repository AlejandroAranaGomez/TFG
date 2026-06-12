package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EjercicioService {

    @GET("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios")
    Call<List<EjercicioDTO>> obtenerEjercicios(@Path("idRutina") Long idRutina, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana);

   @POST("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios")
   Call<Void> anhadirEjercicio(@Path("idRutina") Long idRutina, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana, @Body EjercicioDTO ejercicioDTO);

   @DELETE("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}")
    Call<Void> borrarEjercicio(@Path("idRutina") Long idRutina, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana, @Path("idEjercicio") Long idEjercicio);

}
