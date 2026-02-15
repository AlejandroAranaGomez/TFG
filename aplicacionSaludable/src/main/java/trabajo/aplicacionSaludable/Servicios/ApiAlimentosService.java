package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import trabajo.aplicacionSaludable.ApisExternas.ApiAlimentosResponse;
import trabajo.aplicacionSaludable.ApisExternas.ApiFood;
import trabajo.aplicacionSaludable.ApisExternas.ApiHints;
import trabajo.aplicacionSaludable.Dtos.ApiAlimentosDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApiAlimentosService {

    private final RestTemplate restTemplate;

    @Value("${edamam.app-id}")
    private String appId;

    @Value("${edamam.app-key}")
    private String appKey;

    public ApiAlimentosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ApiAlimentosDTO> buscarAlimentos(String query) {

        String url =
                "https://api.edamam.com/api/food-database/v2/parser"
                        + "?app_id=" + appId
                        + "&app_key=" + appKey
                        + "&ingr=" + query;

        ApiAlimentosResponse response =
                restTemplate.getForObject(url, ApiAlimentosResponse.class);

        List<ApiAlimentosDTO> resultado = new ArrayList<>();

        if (response == null || response.getHints() == null) {
            return resultado;
        }

        for (ApiHints hint : response.getHints()) {
            ApiFood food = hint.getFood();
            if (food == null || food.getNutrients() == null) continue;

            ApiAlimentosDTO dto = new ApiAlimentosDTO();
            dto.setNombre(food.getLabel());
            dto.setCalorias(food.getNutrients().getCalorias().floatValue());
            dto.setProteinas(food.getNutrients().getProteinas().floatValue());
            dto.setCarbohidratos(food.getNutrients().getCarbohidratos().floatValue());
            dto.setGrasas(food.getNutrients().getGrasas().floatValue());

            resultado.add(dto);
        }

        return resultado;
    }
}
