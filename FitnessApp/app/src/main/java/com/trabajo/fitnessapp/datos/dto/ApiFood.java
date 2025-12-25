package com.trabajo.fitnessapp.datos.dto;

import com.google.gson.annotations.SerializedName;

public class ApiFood {
    @SerializedName("label")
    private String label;

    @SerializedName("image")
    private String image;

    @SerializedName("nutrients")
    private ApiNutrients nutrients;

    public String getLabel() {
        return label;
    }
    public String getImage() {
        return image;
    }
    public ApiNutrients getNutrients() {
        return nutrients;
    }
}
