package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina.DiaPerteneceAOtraRutinaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina.DiaRutinaYaCreadoException;
import trabajo.aplicacionSaludable.Servicios.DiaEnRutinaService;

import java.util.List;

@RestController
@RequestMapping("/api/diasEnRutina")
public class DiaEnRutinaController {

    @Autowired
    private DiaEnRutinaService diaEnRutinaService;

    @PostMapping("/rutinas/{idRutinaCompleta}")
    public ResponseEntity<?> crearDiaEnRutina(@PathVariable Long idRutinaCompleta, @RequestBody DiaEnRutinaDTO diaEnRutinaDTO) {
        try {
            DiaEnRutinaDTO nuevoDia = diaEnRutinaService.crearDiaEnRutina(diaEnRutinaDTO, idRutinaCompleta);
            if (nuevoDia == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
            }
            return new ResponseEntity<>(nuevoDia,HttpStatus.CREATED);
        } catch (DiaRutinaYaCreadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/rutinas/{idRutinaCompleta}")
    public ResponseEntity<?> obtenerDias(@PathVariable Long idRutinaCompleta) {
        List<DiaEnRutinaDTO> dias = diaEnRutinaService.listaDiaEnRutina(idRutinaCompleta);
        if (dias == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Rutina no encontrada");
        }
        return ResponseEntity.ok(dias);
    }

    @PutMapping("/{idDiaEnRutina}/rutinas/{idRutinaCompleta}")
    public ResponseEntity<?> actualizarDia(@PathVariable Long idDiaEnRutina, @PathVariable Long idRutinaCompleta, @RequestBody DiaEnRutinaDTO diaEnRutinaDTO) {
        try {
            DiaEnRutinaDTO diaActualizado = diaEnRutinaService.editarDiaEnRutina(diaEnRutinaDTO, idRutinaCompleta, idDiaEnRutina);
            if (diaActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return ResponseEntity.ok(diaActualizado);
        } catch (DiaPerteneceAOtraRutinaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idDiaEnRutina}/rutinas/{idRutinaCompleta}")
    public  ResponseEntity<?> borrarDia(@PathVariable Long idDiaEnRutina, @PathVariable Long idRutinaCompleta) {
        try {
            boolean eliminado = diaEnRutinaService.borrarDia(idDiaEnRutina, idRutinaCompleta);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return new ResponseEntity<>("Dia Borrado", HttpStatus.OK);
        } catch (DiaPerteneceAOtraRutinaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
