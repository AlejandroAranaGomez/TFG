package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

// Clases para conseguir los alimentos de la api

public class ApiHints {
    @JsonProperty("food")
    private ApiFood food;

    public ApiFood getFood() {
        return food;
    }
}
