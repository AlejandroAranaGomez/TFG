package com.trabajo.fitnessapp.datos.dto;

import com.trabajo.fitnessapp.dominio.Genero;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioPerfilDTO {

    private Long idUsuario;

    private String nombre;
    private String apellido1;
    private String apellido2;

    private String fechaNacimiento;

    private float peso;
    private int altura;

    private Objetivo objetivo;
    private NivelDeActividad nivelDeActividad;

    private String email;
    private String telefono;
    private Genero genero;
}
