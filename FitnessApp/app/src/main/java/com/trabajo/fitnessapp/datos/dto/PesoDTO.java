package com.trabajo.fitnessapp.datos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PesoDTO {

    private float peso;

    public PesoDTO() {

    }

    public PesoDTO(float peso) {
        this.peso = peso;
    }

}
