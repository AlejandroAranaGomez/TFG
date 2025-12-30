package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.InicioSesionDTO;
import com.trabajo.fitnessapp.datos.dto.RegistroDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AutorizacionService {

    @POST("api/usuarios/registrar")
    Call<UsuarioDTO> registrarUsuario(@Body RegistroDTO registroDTO);

    @POST("api/usuarios/login")
    Call<UsuarioDTO> iniciarSesion(@Body InicioSesionDTO inicioSesionDTO);

}
