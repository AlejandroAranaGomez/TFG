package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trabajo.aplicacionSaludable.Dtos.ApiEjercicioDTO;
import trabajo.aplicacionSaludable.Servicios.ApiEjerciciosService;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios/buscar")
@CrossOrigin
public class BuscaEjerciciosController {

    @Autowired
    private ApiEjerciciosService apiEjerciciosService;

    @GetMapping
    public List<ApiEjercicioDTO> obtenerEjercicios() {
        return apiEjerciciosService.obtenerEjercicios();
    }
}
