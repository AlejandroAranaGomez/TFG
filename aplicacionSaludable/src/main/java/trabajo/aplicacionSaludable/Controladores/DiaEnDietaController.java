package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta.DiaPerteneceAOtraDietaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta.DiaYaCreadoException;
import trabajo.aplicacionSaludable.Servicios.DiaEnDietaService;

import java.util.List;

@RestController
@RequestMapping("/api/diasEnDieta")
public class DiaEnDietaController {

    @Autowired
    private DiaEnDietaService diaEnDietaService;

    @PostMapping("/dietas/{idDietaCompleta}")
    public ResponseEntity<?> crearDiaEnDieta(@PathVariable Long idDietaCompleta, @RequestBody DiaEnDietaDTO diaEnDietaDTO) {
        try {
            DiaEnDietaDTO nuevoDia = diaEnDietaService.crearDiaEnDieta(diaEnDietaDTO, idDietaCompleta);
            if (nuevoDia == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
            }
            return new ResponseEntity<>(nuevoDia,HttpStatus.CREATED);
        } catch (DiaYaCreadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/dietas/{idDietaCompleta}")
    public ResponseEntity<?> obtenerDias(@PathVariable Long idDietaCompleta) {
        List<DiaEnDietaDTO> dias = diaEnDietaService.listaDiaEnDieta(idDietaCompleta);
        if (dias == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dieta no encontrada");
        }
        return ResponseEntity.ok(dias);
    }

    @PutMapping("/{idDiaDieta}/dietas/{idDietaCompleta}")
    public ResponseEntity<?> actualizarDia(@PathVariable Long idDiaDieta, @PathVariable Long idDietaCompleta ,@RequestBody DiaEnDietaDTO diaEnDietaDTO) {
        try {
            DiaEnDietaDTO diaActualizado = diaEnDietaService.editarDiaEnDieta(diaEnDietaDTO, idDietaCompleta, idDiaDieta);
            if (diaActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
            }
            return ResponseEntity.ok(diaActualizado);

        } catch (DiaPerteneceAOtraDietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idDiaDieta}/dietas/{idDietaCompleta}")
    public ResponseEntity<?> borrarDia(@PathVariable Long idDiaDieta, @PathVariable Long idDietaCompleta) {
        try {
            boolean eliminado = diaEnDietaService.borrarDia(idDiaDieta, idDietaCompleta);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Día no encontrado");
            }
            return new ResponseEntity<>("Dia borrado",HttpStatus.OK);
        } catch (DiaPerteneceAOtraDietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }



}
