package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ApiAlimentosDTO;
import trabajo.aplicacionSaludable.Servicios.ApiAlimentosService;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos/buscar")
@CrossOrigin
public class BuscaAlimentosController {

    @Autowired
    private ApiAlimentosService apiAlimentosService;

    @GetMapping
    public List<ApiAlimentosDTO> buscar(@RequestParam String query) {
        return apiAlimentosService.buscarAlimentos(query);
    }
}
