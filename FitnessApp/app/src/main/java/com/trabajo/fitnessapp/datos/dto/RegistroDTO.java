package com.trabajo.fitnessapp.datos.dto;

import com.trabajo.fitnessapp.dominio.Genero;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegistroDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String fechaNacimiento;
    private Genero genero;
    private float peso;
    private int altura;
    private String email;
    private String telefono;
    private String contrasenha;
    private Objetivo objetivo;
    private NivelDeActividad nivelDeActividad;

}
