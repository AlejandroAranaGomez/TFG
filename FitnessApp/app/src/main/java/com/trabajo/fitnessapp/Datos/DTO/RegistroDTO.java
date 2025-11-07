package com.trabajo.fitnessapp.Datos.DTO;

import com.trabajo.fitnessapp.Datos.Model.Generos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
