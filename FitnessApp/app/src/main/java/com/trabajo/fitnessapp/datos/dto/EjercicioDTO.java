package com.trabajo.fitnessapp.datos.dto;

import com.trabajo.fitnessapp.dominio.Musculo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EjercicioDTO {
    private Long idEjercicio;
    private Long idEjercicioEnDiaRutina;
    private String nombre;
    private Musculo musculoEnfocado;
    private String idApi;

    public EjercicioDTO() {

    }

    public EjercicioDTO(Long idEjercicio, Long idEjercicioEnDiaRutina, String nombre, Musculo musculoEnfocado, String idApi) {
        this.idEjercicio = idEjercicio;
        this.idEjercicioEnDiaRutina = idEjercicioEnDiaRutina;
        this.nombre = nombre;
        this.musculoEnfocado = musculoEnfocado;
        this.idApi = idApi;
    }
}
