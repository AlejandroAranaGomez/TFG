package com.trabajo.fitnessapp.datos.dto;

import com.trabajo.fitnessapp.dominio.Generos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RegistroDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String fechaNacimiento;
    private Generos genero;
    private float peso;
    private float altura;
    private String email;
    private String telefono;
    private String contrasenha;

}
