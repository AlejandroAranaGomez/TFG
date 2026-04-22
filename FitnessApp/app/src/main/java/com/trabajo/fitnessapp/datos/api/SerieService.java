package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.SerieDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SerieService {
    @GET("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}/series")
    Call<List<SerieDTO>> obtenerSeries(@Path("idEjercicio") Long idEjercicio);

    @POST("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}/series")
    Call<SerieDTO> crearSerie(@Path("idEjercicio") Long idEjercicio, @Body SerieDTO serieDTO);

    @PUT("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}/series/{idSerie}")
    Call<SerieDTO> actualizarSerie(@Path("idSerie") Long idSerie, @Body SerieDTO serieDTO);

    @DELETE("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}/series/{idSerie}")
    Call<Void> borrarSerie(@Path("idSerie") Long idSerie);
}
