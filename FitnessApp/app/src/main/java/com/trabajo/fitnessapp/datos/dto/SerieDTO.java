package com.trabajo.fitnessapp.datos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SerieDTO {
    private Long idSerie;

    private String serieAnterior;
    private int repeticiones;
    private float peso;

    public SerieDTO() {

    }

    public SerieDTO(Long idSerie, String serieAnterior, int repeticiones, float peso) {
        this.idSerie = idSerie;
        this.serieAnterior = serieAnterior;
        this.repeticiones = repeticiones;
        this.peso = peso;
    }
}
