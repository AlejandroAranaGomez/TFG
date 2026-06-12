package trabajo.aplicacionSaludable.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ComidaSeguimientoDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroComidaDiariaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento.ComidaNoRegistradaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento.ComidaYaRegistradaException;
import trabajo.aplicacionSaludable.Servicios.ComidaService;
import trabajo.aplicacionSaludable.Servicios.RegistroComidaDiariaService;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento/{idUsuario}")
public class SeguimientoController {

    private ComidaService comidaService;
    private RegistroComidaDiariaService registroComidaDiariaService;

    public SeguimientoController(ComidaService comidaService, RegistroComidaDiariaService registroComidaDiariaService) {
        this.comidaService = comidaService;
        this.registroComidaDiariaService = registroComidaDiariaService;
    }

    @GetMapping
    public ResponseEntity<?> obtenerComidasHoy(@PathVariable Long idUsuario) {

        List<ComidaSeguimientoDTO> comidas = comidaService.obtenerComidasHoy(idUsuario);

        if (comidas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay dieta activa");
        }

        return ResponseEntity.ok(comidas);
    }

    @PostMapping("/comida/{idComida}")
    public ResponseEntity<?> registrarComidaRealizada(@PathVariable Long idUsuario, @PathVariable Long idComida) {
        try {
            RegistroComidaDiariaDTO registro = registroComidaDiariaService.registrarComidaHoy(idUsuario, idComida);

            if (registro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se pudo registrar la comida");
            }
    
            return ResponseEntity.ok(registro);
        } catch (ComidaYaRegistradaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/comida/{idComida}")
    public ResponseEntity<?> eliminarComidaRealizada(@PathVariable Long idUsuario, @PathVariable Long idComida) {
        try {
            boolean eliminado = registroComidaDiariaService.eliminarComidaHoy(idUsuario, idComida);

            if (eliminado) {
                return ResponseEntity.ok("Comida eliminada");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontró la comida o el usuario.");
            }
        } catch (ComidaNoRegistradaException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la comida registrada.");
        }
    }

}
