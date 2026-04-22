package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiasEnDietaService {

    @PUT("api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}")
    Call<DiaEnDietaDTO> guardarDia(
            @Path("idDieta") Long idDietaCompleta,
            @Path("diaDeLaSemana") DiaDeLaSemana diaDeLaSemana,
            @Body DiaEnDietaDTO diaEnDietaDTO
    );

    @DELETE("api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}")
    Call<Void> borrarDia(
            @Path("idDieta") Long idDietaCompleta,
            @Path("diaDeLaSemana") DiaDeLaSemana diaDeLaSemana
    );

}
