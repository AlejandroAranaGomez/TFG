package com.trabajo.fitnessapp.Datos.Api;

import com.trabajo.fitnessapp.Datos.DTO.InicioSesionDTO;
import com.trabajo.fitnessapp.Datos.DTO.RegistroDTO;
import com.trabajo.fitnessapp.Datos.Model.UsuarioDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AutorizacionService {

    @POST("api/autentification/registrar")
    Call<UsuarioDTO> registrarUsuario(@Body RegistroDTO registroDTO);

    @POST("api/autentification/login")
    Call<UsuarioDTO> iniciarSesion(@Body InicioSesionDTO inicioSesionDTO);

}
