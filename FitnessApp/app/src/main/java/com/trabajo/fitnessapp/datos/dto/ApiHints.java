package com.trabajo.fitnessapp.datos.dto;

import com.google.gson.annotations.SerializedName;

public class ApiHints {
    @SerializedName("food")
    private ApiFood food;

    public ApiFood getFood() {
        return food;
    }
}
