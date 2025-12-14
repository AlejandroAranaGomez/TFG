package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;
import trabajo.aplicacionSaludable.Servicios.DiaEnDietaService;
import trabajo.aplicacionSaludable.Servicios.DietaCompletaService;

import java.util.List;

@RestController
@RequestMapping("/api/diasEnDieta")
public class DiaEnDietaController {

    @Autowired
    private DiaEnDietaService diaEnDietaService;

    @PostMapping("/dietas/{idDietaCompleta}")
    public ResponseEntity<?> crearDiaEnDieta(@PathVariable Long idDietaCompleta, @RequestBody DiaEnDietaDTO diaEnDietaDTO) throws Exception {
        try {
            DiaEnDietaDTO nuevoDia = diaEnDietaService.crearDiaEnDieta(diaEnDietaDTO, idDietaCompleta);
            return new ResponseEntity<>(nuevoDia,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dietas/{idDietaCompleta}")
    public ResponseEntity<?> obtenerDias(@PathVariable Long idDietaCompleta) throws Exception {
        try {
            List<DiaEnDietaDTO> dias = diaEnDietaService.listaDiaEnDieta(idDietaCompleta);
            return ResponseEntity.ok(dias);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idDiaDieta}/dietas/{idDietaCompleta}")
    public ResponseEntity<?> actualizarDia(@PathVariable Long idDiaDieta, @PathVariable Long idDietaCompleta ,@RequestBody DiaEnDietaDTO diaEnDietaDTO) throws Exception {
        try {
            DiaEnDietaDTO diaActualizado = diaEnDietaService.editarDiaEnDieta(diaEnDietaDTO, idDietaCompleta, idDiaDieta);
            return ResponseEntity.ok(diaActualizado);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idDiaDieta}/dietas/{idDietaCompleta}")
    public ResponseEntity<?> borrarDia(@PathVariable Long idDiaDieta, @PathVariable Long idDietaCompleta) throws Exception {
        try {
            diaEnDietaService.borrarDia(idDiaDieta, idDietaCompleta);
            return new ResponseEntity<>("Dia borrado",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



}
