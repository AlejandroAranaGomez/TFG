package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;
import trabajo.aplicacionSaludable.Servicios.DietaCompletaService;

import java.util.List;


@RestController
@RequestMapping("/api/dietasCompletas")
public class DietaCompletaController {

    @Autowired
    private DietaCompletaService dietaCompletaService;

    @PostMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> crearDietasCompletas(@PathVariable Long idUsuario, @RequestBody DietaCompletaDTO dietaCompletaDTO) throws Exception {
        try {
            DietaCompletaDTO nuevaDieta = dietaCompletaService.creaDietaCompleta(idUsuario, dietaCompletaDTO);
            return new ResponseEntity<>(nuevaDieta, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> obtenerDietasCompletas(@PathVariable Long idUsuario) throws Exception {
        try {
            List<DietaCompletaDTO> dietas = dietaCompletaService.listaDietaCompletaUsuario(idUsuario);
            return ResponseEntity.ok(dietas);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idDietaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> actualizarDieta(@PathVariable Long idDietaCompleta, @PathVariable Long idUsuario, @RequestBody DietaCompletaDTO dietaCompletaDTO) throws Exception {
        try {
            DietaCompletaDTO dietaActualizada = dietaCompletaService.actualizaDietaCompleta(dietaCompletaDTO, idUsuario, idDietaCompleta);
            return ResponseEntity.ok(dietaActualizada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idDietaCompleta}/usuarios/{idUsuario}")
    public ResponseEntity<?> borrarDietaCompleta(@PathVariable Long idDietaCompleta, @PathVariable Long idUsuario) throws Exception {
        try {
            dietaCompletaService.borraDietaCompleta(idUsuario, idDietaCompleta);
            return new ResponseEntity<>("Dieta borrada",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
