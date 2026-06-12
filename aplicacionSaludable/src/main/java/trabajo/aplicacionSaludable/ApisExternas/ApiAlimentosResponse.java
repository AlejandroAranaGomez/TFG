package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Clases para conseguir los alimentos de la api

public class ApiAlimentosResponse {
    @JsonProperty("hints")
    private List<ApiHints> hints;

    public List<ApiHints> getHints() {
        return hints;
    }

}
