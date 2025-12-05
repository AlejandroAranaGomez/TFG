package com.trabajo.fitnessapp.datos.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiAlimentosResponse {
    @SerializedName("hints")
    private List<ApiHints> hints;

    public List<ApiHints> getHints() {
        return hints;
    }

}
