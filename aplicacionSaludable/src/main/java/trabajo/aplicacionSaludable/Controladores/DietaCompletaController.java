package trabajo.aplicacionSaludable.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDuplicadaException;
import trabajo.aplicacionSaludable.Servicios.DietaCompletaService;

import java.util.List;


@RestController
@RequestMapping("/api/usuarios/{idUsuario}/dietas")
public class DietaCompletaController {

    private DietaCompletaService dietaCompletaService;

    public DietaCompletaController(DietaCompletaService dietaCompletaService) {
        this.dietaCompletaService = dietaCompletaService;
    }

    @PostMapping
    public ResponseEntity<?> crearDietasCompletas(@PathVariable Long idUsuario, @RequestBody DietaCompletaDTO dietaCompletaDTO) {
        try {
            DietaCompletaDTO nuevaDieta = dietaCompletaService.creaDietaCompleta(idUsuario, dietaCompletaDTO);
            if  (nuevaDieta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            return new ResponseEntity<>(nuevaDieta, HttpStatus.CREATED);
        } catch (DietaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerDietasCompletas(@PathVariable Long idUsuario) {
        List<DietaCompletaDTO> dietas = dietaCompletaService.listaDietaCompletaUsuario(idUsuario);
        if (dietas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(dietas);
    }

    @PutMapping("/{idDieta}")
    public ResponseEntity<?> actualizarDieta(@PathVariable Long idDieta, @PathVariable Long idUsuario, @RequestBody DietaCompletaDTO dietaCompletaDTO) {
        try {
            DietaCompletaDTO dietaActualizada = dietaCompletaService.actualizaDietaCompleta(dietaCompletaDTO, idUsuario, idDieta);
            if (dietaActualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }
            return ResponseEntity.ok(dietaActualizada);
        } catch (DietaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DietaDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idDieta}")
    public ResponseEntity<?> borrarDietaCompleta(@PathVariable Long idDieta, @PathVariable Long idUsuario) {
        try {
            boolean eliminada = dietaCompletaService.borraDietaCompleta(idUsuario, idDieta);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }
            return new ResponseEntity<>("Dieta borrada",HttpStatus.OK);
        } catch (DietaDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/{idDieta}")
    public ResponseEntity<?> obtenerDieta(@PathVariable Long idDieta, @PathVariable Long idUsuario) {
        try {
            DietaCompletaDTO dieta = dietaCompletaService.obtenerDieta(idUsuario, idDieta);
            if (dieta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }
            return ResponseEntity.ok(dieta);
        } catch (DietaDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping("/{idDieta}/estado")
    public ResponseEntity<?> activarDieta(@PathVariable Long idUsuario, @PathVariable Long idDieta) {
        try {
            boolean activar = dietaCompletaService.activarDieta(idUsuario, idDieta);

            if (!activar) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }

            return ResponseEntity.ok("Dieta activada");
        } catch (DietaDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
