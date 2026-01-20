package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiasEnRutinaService {

    @GET("api/diasEnRutina/rutinas/{idRutinaCompleta}")
    Call<List<DiaEnRutinaDTO>> obtenerDiasEnRutina(@Path("idRutinaCompleta") Long idRutinaCompleta);

    @PUT("api/diasEnRutina/{idDiaEnRutina}/rutinas/{idRutinaCompleta}")
    Call<DiaEnRutinaDTO> editarDia(@Path("idDiaEnRutina") Long idDiaEnRutina,
                                   @Path("idRutinaCompleta") Long idRutinaCompleta,
                                   @Body DiaEnRutinaDTO diaEnRutinaDTO);

    @POST("api/diasEnRutina/rutinas/{idRutinaCompleta}")
    Call<DiaEnRutinaDTO> crearDia(@Path("idRutinaCompleta") Long idRutinaCompleta,
                                  @Body DiaEnRutinaDTO diaEnRutinaDTO);

    @DELETE("api/diasEnRutina/{idDiaEnRutina}/rutinas/{idRutinaCompleta}")
    Call<Void> borrarDia(@Path("idDiaEnRutina") Long idDiaEnRutina,
                         @Path("idRutinaCompleta") Long idRutinaCompleta);

}
