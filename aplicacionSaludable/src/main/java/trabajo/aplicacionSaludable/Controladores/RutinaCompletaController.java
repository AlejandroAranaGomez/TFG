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
@RequestMapping("/api/rutinasCompletas")
public class RutinaCompletaController {

    @Autowired
    private RutinaCompletaService rutinaCompletaService;

    @PostMapping("/usuarios/{idUsuario}")
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

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> listaRutinas(@PathVariable Long idUsuario) {
        List<RutinaCompletaDTO> rutinas = rutinaCompletaService.listarRutinasUsuario(idUsuario);

        if (rutinas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(rutinas);
    }

    @PutMapping("/{idRutinaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> actualizarRutina(@PathVariable Long idUsuario, @PathVariable Long idRutinaCompleta, @RequestBody RutinaCompletaDTO rutinaCompletaDTO) {
        try {
            RutinaCompletaDTO rutinaActualizada = rutinaCompletaService.actualizarRutina(idUsuario, idRutinaCompleta, rutinaCompletaDTO);
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

    @DeleteMapping("/{idRutinaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> borraRutina(@PathVariable Long idUsuario, @PathVariable Long idRutinaCompleta) {
        try {
            boolean eliminada = rutinaCompletaService.borrarRutina(idUsuario, idRutinaCompleta);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
            }
            return new ResponseEntity<>("Rutina borrada",HttpStatus.OK);
        } catch (RutinaOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
