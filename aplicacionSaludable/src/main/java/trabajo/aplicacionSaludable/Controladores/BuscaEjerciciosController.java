package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trabajo.aplicacionSaludable.Dtos.ApiEjercicioDTO;
import trabajo.aplicacionSaludable.Servicios.EjercicioService;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
@CrossOrigin
public class BuscaEjerciciosController {

    private EjercicioService ejercicioService;

    public BuscaEjerciciosController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
    }

    @GetMapping
    public List<ApiEjercicioDTO> obtenerEjercicios() {
        return ejercicioService.obtenerEjercicios();
    }
}
