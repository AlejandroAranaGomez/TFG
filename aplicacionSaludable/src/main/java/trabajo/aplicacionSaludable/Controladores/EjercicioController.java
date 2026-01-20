package trabajo.aplicacionSaludable.Controladores;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;
import trabajo.aplicacionSaludable.Servicios.EjercicioService;

import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioController {

    @Autowired
    EjercicioService ejercicioService;

    @GetMapping("/dias/{idDiaEnRutina}")
    public ResponseEntity<?> anhadirEjercicio(@PathVariable Long idDiaEnRutina) {
        List<EjercicioDTO> ejercicios = ejercicioService.listaEjercicios(idDiaEnRutina);
        if (ejercicios == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }
        return ResponseEntity.ok(ejercicios);
    }

    @PostMapping("/dias/{idDiaEnRutina}")
    public ResponseEntity<Void> anhadirEjercicio(@PathVariable Long idDiaEnRutina, @RequestBody EjercicioDTO ejercicioDTO) {
        ejercicioService.anhadirEjercicio(idDiaEnRutina, ejercicioDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idEjercicio}/dias/{idDiaEnRutina}")
    public  ResponseEntity<?> borrarEjercicio(@PathVariable Long idDiaEnRutina, @PathVariable Long idEjercicio) {
        boolean eliminado = ejercicioService.borrarEjercicio(idDiaEnRutina, idEjercicio);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }

        return new ResponseEntity<>("Ejercicio borrado", HttpStatus.OK);
    }

}
