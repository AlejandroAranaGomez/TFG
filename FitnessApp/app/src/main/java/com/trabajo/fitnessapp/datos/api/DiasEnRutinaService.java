package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiasEnRutinaService {
    @PUT("api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}")
    Call<DiaEnRutinaDTO> editarDia(
            @Path("diaDeLaSemana") DiaDeLaSemana diaDeLaSemana,
            @Path("idRutina") Long idRutinaCompleta,
            @Body DiaEnRutinaDTO diaEnRutinaDTO
    );

    @DELETE("api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}")
    Call<Void> borrarDia(
            @Path("diaDeLaSemana") DiaDeLaSemana diaDeLaSemana,
            @Path("idRutina") Long idRutinaCompleta
    );

}
