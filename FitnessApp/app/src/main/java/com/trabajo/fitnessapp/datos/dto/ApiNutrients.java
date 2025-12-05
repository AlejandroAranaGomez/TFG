package com.trabajo.fitnessapp.datos.dto;

import com.google.gson.annotations.SerializedName;

public class ApiNutrients {

    @SerializedName("ENERC_KCAL")
    private Double calorias;

    @SerializedName("PROCNT")
    private Double proteinas;

    @SerializedName("FAT")
    private Double grasas;

    @SerializedName("CHOCDF")
    private Double carbohidratos;

    // Si es nulo devolvemos 0.
    public Double getCalorias() {
        return calorias != null ? calorias : 0.0;
    }
    public Double getProteinas() {
        return proteinas != null ? proteinas : 0.0;
    }
    public Double getGrasas() {
        return grasas != null ? grasas : 0.0;
    }
    public Double getCarbohidratos() {
        return carbohidratos != null ? carbohidratos : 0.0;
    }
}
