package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDuplicadaException;
import trabajo.aplicacionSaludable.Servicios.DietaCompletaService;

import java.util.List;


@RestController
@RequestMapping("/api/dietasCompletas")
public class DietaCompletaController {

    @Autowired
    private DietaCompletaService dietaCompletaService;

    @PostMapping("/usuarios/{idUsuario}")
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

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> obtenerDietasCompletas(@PathVariable Long idUsuario) {
        List<DietaCompletaDTO> dietas = dietaCompletaService.listaDietaCompletaUsuario(idUsuario);
        if (dietas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        return ResponseEntity.ok(dietas);
    }

    @PutMapping("/{idDietaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> actualizarDieta(@PathVariable Long idDietaCompleta, @PathVariable Long idUsuario, @RequestBody DietaCompletaDTO dietaCompletaDTO) {
        try {
            DietaCompletaDTO dietaActualizada = dietaCompletaService.actualizaDietaCompleta(dietaCompletaDTO, idUsuario, idDietaCompleta);
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

    @DeleteMapping("/{idDietaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> borrarDietaCompleta(@PathVariable Long idDietaCompleta, @PathVariable Long idUsuario) {
        try {
            boolean eliminada = dietaCompletaService.borraDietaCompleta(idUsuario, idDietaCompleta);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }
            return new ResponseEntity<>("Dieta borrada",HttpStatus.OK);
        } catch (DietaDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


}
