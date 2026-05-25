package trabajo.aplicacionSaludable.Controladores;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesEjercicios.EjercicioDuplicadoException;
import trabajo.aplicacionSaludable.Servicios.EjercicioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/rutinas/{idRutina}/{diaDeLaSemana}/ejercicios")
public class EjercicioController {

    EjercicioService ejercicioService;

    public EjercicioController(EjercicioService ejercicioService) {
        this.ejercicioService = ejercicioService;
    }

    @GetMapping
    public ResponseEntity<?> listaEjercicio(@PathVariable Long idRutina, @PathVariable DiaDeLaSemana diaDeLaSemana) {
        List<EjercicioDTO> ejercicios = ejercicioService.listaEjercicios(idRutina, diaDeLaSemana);
        if (ejercicios == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }
        return ResponseEntity.ok(ejercicios);
    }

    @PostMapping
    public ResponseEntity<?> anhadirEjercicio(@PathVariable Long idRutina, @PathVariable DiaDeLaSemana diaDeLaSemana, @RequestBody EjercicioDTO ejercicioDTO) {
        try {
            ejercicioService.anhadirEjercicio(idRutina, diaDeLaSemana, ejercicioDTO);
            return ResponseEntity.ok().build();
        } catch (EjercicioDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idEjercicio}")
    public  ResponseEntity<?> borrarEjercicio(@PathVariable Long idRutina, @PathVariable DiaDeLaSemana diaDeLaSemana, @PathVariable Long idEjercicio) {
        boolean eliminado = ejercicioService.borrarEjercicio(idRutina, diaDeLaSemana, idEjercicio);
        if (!eliminado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }

        return new ResponseEntity<>("Ejercicio borrado", HttpStatus.OK);
    }

}
