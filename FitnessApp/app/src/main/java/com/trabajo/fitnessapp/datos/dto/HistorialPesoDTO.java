package com.trabajo.fitnessapp.datos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistorialPesoDTO {

    private float peso;
    private String fecha;

    public HistorialPesoDTO() {

    }

    public HistorialPesoDTO(float peso, String fecha) {
        this.peso = peso;
        this.fecha = fecha;
    }
}
