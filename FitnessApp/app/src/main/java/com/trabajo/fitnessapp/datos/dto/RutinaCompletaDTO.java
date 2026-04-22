package com.trabajo.fitnessapp.datos.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RutinaCompletaDTO implements Serializable {

    private Long idRutinaCompleta;

    private String nombreRutinaCompleta;
    private String resumen;

    private List<DiaEnRutinaDTO> dias;

    public RutinaCompletaDTO() {

    }

    public RutinaCompletaDTO(Long idRutinaCompleta, String nombreRutinaCompleta, String resumen) {
        this.idRutinaCompleta = idRutinaCompleta;
        this.nombreRutinaCompleta = nombreRutinaCompleta;
        this.resumen = resumen;
    }

}
