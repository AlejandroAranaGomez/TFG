package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaPerteneceAOtroDiaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaYaExisteException;
import trabajo.aplicacionSaludable.Servicios.ComidaService;

import java.util.List;

@RestController
@RequestMapping("/api/comidas")
public class ComidaController {

    @Autowired
    ComidaService comidaService;

    @PostMapping("/dias/{idDiaEnDieta}")
    public ResponseEntity<?> crearComida(@PathVariable Long idDiaEnDieta, @RequestBody ComidaDTO comidaDTO) {
        try {
            ComidaDTO nuevaComida = comidaService.crearComida(idDiaEnDieta, comidaDTO);

            if (nuevaComida == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Día no encontrado");
            }
            return new ResponseEntity<>(nuevaComida, HttpStatus.CREATED);
        } catch (ComidaYaExisteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/dias/{idDiaEnDieta}")
    public ResponseEntity<?> obtenerComidas(@PathVariable Long idDiaEnDieta) {
        List<ComidaDTO> comidas = comidaService.listaComidas(idDiaEnDieta);
        if (comidas == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dia no encontrado");
        }
        return ResponseEntity.ok(comidas);
    }

    @PutMapping("/{idComida}/dias/{idDiaEnDieta}")
    public ResponseEntity<?> editarComida(@PathVariable Long idComida, @PathVariable Long idDiaEnDieta, @RequestBody ComidaDTO comidaDTO) {
        try {
            ComidaDTO comidaEditada = comidaService.editarComida(idDiaEnDieta, idComida, comidaDTO);
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

    @DeleteMapping("/{idComida}/dias/{idDiaEnDieta}")
    public ResponseEntity<?> borrarComida(@PathVariable Long idComida, @PathVariable Long idDiaEnDieta) {
        try {
            boolean eliminada = comidaService.borrarComida(idDiaEnDieta, idComida);
            if (!eliminada) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
            }
            return new ResponseEntity<>("Comida borrada", HttpStatus.OK);
        } catch (ComidaPerteneceAOtroDiaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
