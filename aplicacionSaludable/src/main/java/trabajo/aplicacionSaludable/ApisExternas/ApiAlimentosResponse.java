package trabajo.aplicacionSaludable.ApisExternas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiAlimentosResponse {
    @JsonProperty("hints")
    private List<ApiHints> hints;

    public List<ApiHints> getHints() {
        return hints;
    }

}
