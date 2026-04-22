package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina.DiaPerteneceAOtraRutinaException;
import trabajo.aplicacionSaludable.Servicios.DiaEnRutinaService;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}")
public class DiaEnRutinaController {

    private DiaEnRutinaService diaEnRutinaService;

    public DiaEnRutinaController(DiaEnRutinaService diaEnRutinaService) {
        this.diaEnRutinaService = diaEnRutinaService;
    }

    @PutMapping
    public ResponseEntity<?> guardarDia(@PathVariable DiaDeLaSemana diaDeLaSemana, @PathVariable Long idRutina, @RequestBody DiaEnRutinaDTO diaEnRutinaDTO) {
        try {
            DiaEnRutinaDTO diaActualizado = diaEnRutinaService.guardarDiaEnRutina(diaEnRutinaDTO, idRutina, diaDeLaSemana);
            if (diaActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return ResponseEntity.ok(diaActualizado);
        } catch (DiaPerteneceAOtraRutinaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping
    public  ResponseEntity<?> borrarDia(@PathVariable DiaDeLaSemana diaDeLaSemana, @PathVariable Long idRutina) {
        try {
            boolean eliminado = diaEnRutinaService.borrarDia(diaDeLaSemana, idRutina);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return new ResponseEntity<>("Dia Borrado", HttpStatus.OK);
        } catch (DiaPerteneceAOtraRutinaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
