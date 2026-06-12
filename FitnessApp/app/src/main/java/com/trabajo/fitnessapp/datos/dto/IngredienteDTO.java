package com.trabajo.fitnessapp.datos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class IngredienteDTO {
    private Long idIngrediente;

    private Long idAlimento;
    private String idAlimentoApi;

    private float cantidadEnGramos;
    private String nombre;
    private float caloriasTotales;
    private float carbohidratos;
    private float proteinas;
    private float grasas;

    public IngredienteDTO() {

    }
    public IngredienteDTO(Long idIngrediente, Long idAlimento, String idAlimentoApi, float cantidadEnGramos, String nombre,  float carbohidratos, float grasas,  float caloriasTotales, float proteinas) {
        this.idAlimento = idAlimento;
        this.idIngrediente = idIngrediente;
        this.idAlimentoApi = idAlimentoApi;
        this.cantidadEnGramos = cantidadEnGramos;
        this.nombre = nombre;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
    }
}
