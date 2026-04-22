package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.SerieDTO;
import trabajo.aplicacionSaludable.Servicios.SerieService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios/{idEjercicio}/series")
public class SerieController {

    private SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping
    public ResponseEntity<?> listaSeries(@PathVariable Long idEjercicio) {
        List<SerieDTO> series = serieService.listaSeries(idEjercicio);

        if (series == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ejercicio no encontrado");
        }
        return ResponseEntity.ok(series);
    }

    @PostMapping
    public ResponseEntity<?> crearSerie(@PathVariable Long idEjercicio, @RequestBody SerieDTO serieDTO) {
        SerieDTO serie = serieService.crearSerie(idEjercicio, serieDTO);

        if (serie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ejercicio no encontrado");
        }
        return new  ResponseEntity<>(serie, HttpStatus.CREATED);
    }

    @PutMapping("/{idSerie}")
    public ResponseEntity<?> actualizarSerie(@PathVariable Long idSerie, @RequestBody SerieDTO serieDTO) {
        SerieDTO serie = serieService.actualizarSerie(idSerie, serieDTO);

        if (serie == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie no encontrada");
        }

        return ResponseEntity.ok(serie);
    }

    @DeleteMapping("/{idSerie}")
    public ResponseEntity<?> borrarSerie(@PathVariable Long idSerie) {
        boolean eliminada = serieService.borrarSerie(idSerie);

        if (!eliminada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serie no encontrada");
        }
        return new ResponseEntity<>("Serie borrada",HttpStatus.OK);
    }
}
