package com.trabajo.fitnessapp.datos.api;


import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;
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

    @GET("api/rutinasCompletas/usuarios/{idUsuario}")
    Call<List<RutinaCompletaDTO>> obtenerRutinaUsuario(@Path("idUsuario") Long idUsuario);

    @PUT("api/rutinasCompletas/{idRutinaCompleta}/usuarios/{idUsuario}")
    Call<RutinaCompletaDTO> editarRutina(
            @Path("idRutinaCompleta") Long idRutinaCompleta,
            @Path("idUsuario") Long idUsuario,
            @Body RutinaCompletaDTO rutinaCompletaDTO
    );

    @POST("api/rutinasCompletas/usuarios/{idUsuario}")
    Call<RutinaCompletaDTO> crearRutina(
            @Path("idUsuario") Long idUsuario,
            @Body RutinaCompletaDTO rutinaCompletaDTO
    );

    @DELETE("api/rutinasCompletas/{idRutinaCompleta}/usuarios/{idUsuario}")
    Call<Void> borrarRutina(
            @Path("idRutinaCompleta") Long idRutinaCompleta,
            @Path("idUsuario") Long idUsuario
    );

}
