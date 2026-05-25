package trabajo.aplicacionSaludable.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaPerteneceAOtroDiaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaYaExisteException;
import trabajo.aplicacionSaludable.Servicios.ComidaService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas")
public class ComidaController {

    ComidaService comidaService;

    public ComidaController(ComidaService comidaService) {
        this.comidaService = comidaService;
    }

    @PostMapping
    public ResponseEntity<?> crearComida(@PathVariable Long idDieta, @PathVariable DiaDeLaSemana diaDeLaSemana, @RequestBody ComidaDTO comidaDTO) {
        try {
            ComidaDTO nuevaComida = comidaService.crearComida(idDieta, diaDeLaSemana, comidaDTO);

            if (nuevaComida == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Día no encontrado");
            }
            return new ResponseEntity<>(nuevaComida, HttpStatus.CREATED);
        } catch (ComidaYaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerComidas(@PathVariable Long idDieta, @PathVariable DiaDeLaSemana diaDeLaSemana) {
        List<ComidaDTO> comidas = comidaService.listaComidas(idDieta, diaDeLaSemana);
        if (comidas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }
        return ResponseEntity.ok(comidas);
    }

    @PutMapping("/{idComida}")
    public ResponseEntity<?> editarComida(@PathVariable Long idDieta, @PathVariable Long idComida, @PathVariable DiaDeLaSemana diaDeLaSemana, @RequestBody ComidaDTO comidaDTO) {
        try {
            ComidaDTO comidaEditada = comidaService.editarComida(idDieta, diaDeLaSemana, idComida, comidaDTO);
            if (comidaEditada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
            }
            return ResponseEntity.ok(comidaEditada);
        }  catch (ComidaPerteneceAOtroDiaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ComidaYaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idComida}")
    public ResponseEntity<?> borrarComida(@PathVariable Long idDieta, @PathVariable Long idComida, @PathVariable DiaDeLaSemana diaDeLaSemana) {
        try {
            boolean eliminada = comidaService.borrarComida(idDieta, diaDeLaSemana, idComida);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
            }
            return new ResponseEntity<>("Comida borrada", HttpStatus.OK);
        } catch (ComidaPerteneceAOtroDiaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
