package com.trabajo.fitnessapp.datos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ComidaDTO {

    private Long idComida;

    private String nombre;
    private float caloriasTotales;
    private float carbohidratos;
    private float proteinas;
    private float grasas;

    public ComidaDTO() {

    }

    public ComidaDTO(Long idComida, String nombre,  float carbohidratos, float grasas,  float caloriasTotales, float proteinas) {
        this.idComida = idComida;
        this.nombre = nombre;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
    }

}
