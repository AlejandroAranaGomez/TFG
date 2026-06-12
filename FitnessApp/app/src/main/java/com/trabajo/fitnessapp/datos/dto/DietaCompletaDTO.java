package com.trabajo.fitnessapp.datos.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DietaCompletaDTO implements Serializable {

    private Long idDietaCompleta;

    private String nombre;
    private String descripcion;
    private float caloriasTotales;
    private float proteinas;
    private float carbohidratos;
    private float grasas;
    private boolean activa;

    private List<DiaEnDietaDTO> dias;

    public DietaCompletaDTO() {

    }

    public DietaCompletaDTO(Long idDietaCompleta, String nombre, String descripcion, float caloriasTotales, float proteinas, float carbohidratos, float grasas, boolean activa) {
        this.idDietaCompleta = idDietaCompleta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.activa = activa;
    }

}
