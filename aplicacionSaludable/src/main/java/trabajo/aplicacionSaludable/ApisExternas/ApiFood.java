package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiFood {

    @JsonProperty("foodId")
    private String foodId;

    @JsonProperty("label")
    private String label;

    @JsonProperty("nutrients")
    private ApiNutrients nutrients;

    public String getFoodId() {return foodId;}
    public String getLabel() {
        return label;
    }
    public ApiNutrients getNutrients() {
        return nutrients;
    }
}