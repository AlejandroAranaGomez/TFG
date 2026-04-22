package com.trabajo.fitnessapp.datos.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroComidaDiariaDTO {

    private Long idRegistroComidaDiaria;
    private String fecha;
    private float caloriasTotales;
    private float proteinas;
    private float carbohidratos;
    private float grasas;


    public RegistroComidaDiariaDTO() {
    }

    public RegistroComidaDiariaDTO(Long idRegistroComidaDiaria, String fecha, float caloriasTotales, float proteinas, float carbohidratos, float grasas) {
        this.idRegistroComidaDiaria = idRegistroComidaDiaria;
        this.fecha = fecha;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }

}
