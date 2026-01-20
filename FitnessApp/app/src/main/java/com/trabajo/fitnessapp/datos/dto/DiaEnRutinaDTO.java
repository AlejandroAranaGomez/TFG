package com.trabajo.fitnessapp.datos.dto;

import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaEnRutinaDTO {
    private Long idDiaEnRutina;

    private String nombre;
    private DiaDeLaSemana diaDeLaSemana;

    public DiaEnRutinaDTO() {

    }

    public DiaEnRutinaDTO(Long idDiaEnRutina, String nombre, DiaDeLaSemana diaDeLaSemana) {
        this.idDiaEnRutina = idDiaEnRutina;
        this.nombre = nombre;
        this.diaDeLaSemana = diaDeLaSemana;
    }
}
