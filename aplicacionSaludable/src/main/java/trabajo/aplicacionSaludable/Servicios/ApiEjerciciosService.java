package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import trabajo.aplicacionSaludable.Dtos.ApiEjercicioDTO;
import trabajo.aplicacionSaludable.Dtos.RespuestaEjerciciosDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiEjerciciosService {

    private final RestTemplate restTemplate;

    public ApiEjerciciosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ApiEjercicioDTO> obtenerEjercicios() {

        String url = "http://localhost:3000/exercises?page=1&limit=1000";

        RespuestaEjerciciosDTO response =
                restTemplate.getForObject(url, RespuestaEjerciciosDTO.class);

        if (response == null || response.getData() == null) {
            return new ArrayList<>();
        }

        return response.getData();
    }


}
