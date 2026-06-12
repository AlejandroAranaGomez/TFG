package trabajo.aplicacionSaludable.Controladores;

import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ApiAlimentosDTO;
import trabajo.aplicacionSaludable.Servicios.AlimentoService;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
@CrossOrigin
public class BuscaAlimentosController {

    private AlimentoService alimentoService;

    public BuscaAlimentosController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    @GetMapping
    public List<ApiAlimentosDTO> buscar(@RequestParam String alimento) {
        return alimentoService.buscarAlimentos(alimento);
    }
}
