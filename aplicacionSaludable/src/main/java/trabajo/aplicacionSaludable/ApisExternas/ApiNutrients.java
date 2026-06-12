package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

// Clases para conseguir los alimentos de la api

public class ApiNutrients {

    @JsonProperty("ENERC_KCAL")
    private Double calorias;

    @JsonProperty("PROCNT")
    private Double proteinas;

    @JsonProperty("FAT")
    private Double grasas;

    @JsonProperty("CHOCDF")
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
