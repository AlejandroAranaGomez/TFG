package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiFood {
    @JsonProperty("label")
    private String label;

    @JsonProperty("image")
    private String image;

    @JsonProperty("nutrients")
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