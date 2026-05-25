package trabajo.aplicacionSaludable.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta.DiaPerteneceAOtraDietaException;
import trabajo.aplicacionSaludable.Servicios.DiaEnDietaService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}")
public class DiaEnDietaController {

    private DiaEnDietaService diaEnDietaService;

    public DiaEnDietaController(DiaEnDietaService diaEnDietaService) {
        this.diaEnDietaService = diaEnDietaService;
    }

    @PutMapping
    public ResponseEntity<?> guardarDia(@PathVariable DiaDeLaSemana diaDeLaSemana, @PathVariable Long idDieta , @RequestBody DiaEnDietaDTO diaEnDietaDTO) {
        try {
            DiaEnDietaDTO diaActualizado = diaEnDietaService.guardarDiaEnDieta(diaEnDietaDTO, idDieta, diaDeLaSemana);
            if (diaActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return ResponseEntity.ok(diaActualizado);

        } catch (DiaPerteneceAOtraDietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> borrarDia(@PathVariable DiaDeLaSemana diaDeLaSemana, @PathVariable Long idDieta) {
        try {
            boolean eliminado = diaEnDietaService.borrarDia(diaDeLaSemana, idDieta);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Día no encontrado");
            }
            return new ResponseEntity<>("Dia borrado",HttpStatus.OK);
        } catch (DiaPerteneceAOtraDietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
