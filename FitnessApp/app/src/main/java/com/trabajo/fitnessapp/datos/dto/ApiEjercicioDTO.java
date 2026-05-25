package com.trabajo.fitnessapp.datos.dto;

import java.util.List;

public class ApiEjercicioDTO {

    private String id;
    private String name;
    private List<String> primaryMuscles;
    private List<String> images;

    public String getId() { return id; }

    public String getName() { return name; }
    public List<String> getPrimaryMuscles() {
        return primaryMuscles;
    }
    public List<String> getImages() {
        return images;
    }

}
