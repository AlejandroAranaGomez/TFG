package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiHints {
    @JsonProperty("food")
    private ApiFood food;

    public ApiFood getFood() {
        return food;
    }
}
