package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.RutinaCompletaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas.RutinaDuplicadaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas.RutinaOtroUsuarioException;
import trabajo.aplicacionSaludable.Servicios.RutinaCompletaService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/rutinas")
public class RutinaCompletaController {

    private RutinaCompletaService rutinaCompletaService;

    public RutinaCompletaController(RutinaCompletaService rutinaCompletaService) {
        this.rutinaCompletaService = rutinaCompletaService;
    }

    @PostMapping
    public ResponseEntity<?> crearRutina(@PathVariable Long idUsuario, @RequestBody RutinaCompletaDTO rutinaCompletaDTO) {
        try {
            RutinaCompletaDTO rutina = rutinaCompletaService.crearRutina(idUsuario, rutinaCompletaDTO);

            if (rutina == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            return new ResponseEntity<>(rutina, HttpStatus.CREATED);
        } catch (RutinaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listaRutinas(@PathVariable Long idUsuario) {
        List<RutinaCompletaDTO> rutinas = rutinaCompletaService.listarRutinasUsuario(idUsuario);

        if (rutinas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(rutinas);
    }

    @PutMapping("/{idRutina}")
    public ResponseEntity<?> actualizarRutina(@PathVariable Long idUsuario, @PathVariable Long idRutina, @RequestBody RutinaCompletaDTO rutinaCompletaDTO) {
        try {
            RutinaCompletaDTO rutinaActualizada = rutinaCompletaService.actualizarRutina(idUsuario, idRutina, rutinaCompletaDTO);
            if (rutinaActualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
            }
            return ResponseEntity.ok(rutinaActualizada);
        } catch (RutinaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RutinaOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idRutina}")
    public ResponseEntity<?> borraRutina(@PathVariable Long idUsuario, @PathVariable Long idRutina) {
        try {
            boolean eliminada = rutinaCompletaService.borrarRutina(idUsuario, idRutina);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
            }
            return new ResponseEntity<>("Rutina borrada",HttpStatus.OK);
        } catch (RutinaOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{idRutina}")
    public ResponseEntity<?> obtenerRutina(@PathVariable Long idRutina, @PathVariable Long idUsuario) {
        try {
            RutinaCompletaDTO rutina = rutinaCompletaService.obtenerRutina(idUsuario, idRutina);
            if (rutina == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
            }
            return ResponseEntity.ok(rutina);
        } catch (RutinaOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
