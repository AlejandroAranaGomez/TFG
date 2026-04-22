package com.trabajo.fitnessapp.datos.api;


import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RutinasService {

    @GET("/api/usuarios/{idUsuario}/rutinas")
    Call<List<RutinaCompletaDTO>> obtenerRutinasUsuario(@Path("idUsuario") Long idUsuario);

    @PUT("/api/usuarios/{idUsuario}/rutinas/{idRutina}")
    Call<RutinaCompletaDTO> actualizarRutina(
            @Path("idRutina") Long idRutinaCompleta,
            @Path("idUsuario") Long idUsuario,
            @Body RutinaCompletaDTO rutinaCompletaDTO
    );

    @POST("/api/usuarios/{idUsuario}/rutinas")
    Call<RutinaCompletaDTO> crearRutina(
            @Path("idUsuario") Long idUsuario,
            @Body RutinaCompletaDTO rutinaCompletaDTO
    );

    @DELETE("/api/usuarios/{idUsuario}/rutinas/{idRutina}")
    Call<Void> borrarRutina(
            @Path("idRutina") Long idRutinaCompleta,
            @Path("idUsuario") Long idUsuario
    );

    @GET("/api/usuarios/{idUsuario}/rutinas/{idRutina}")
    Call<RutinaCompletaDTO> obtenerRutina(
            @Path("idUsuario") Long idUsuario,
            @Path("idRutina") Long idRutina
    );

}
