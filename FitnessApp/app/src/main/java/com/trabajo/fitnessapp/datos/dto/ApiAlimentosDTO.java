package com.trabajo.fitnessapp.datos.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiAlimentosDTO {

    private String idApi;
    private String nombre;
    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

}

