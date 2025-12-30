package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.CantidadNegativaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteNoPerteneceADietaException;
import trabajo.aplicacionSaludable.Servicios.IngredienteService;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping("/comidas/{idComida}")
    public ResponseEntity<?> crearIngrediente(@PathVariable Long idComida, @RequestBody IngredienteDTO ingredienteDTO) {
        try {
            IngredienteDTO ingredienteNuevo = ingredienteService.crearIngrediente(idComida, ingredienteDTO);
            if (ingredienteNuevo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
            }
            return new ResponseEntity<>(ingredienteNuevo, HttpStatus.CREATED);
        } catch (CantidadNegativaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IngredienteDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/comidas/{idComida}")
    public ResponseEntity<?> obtenerIngredientes(@PathVariable Long idComida) {

        List<IngredienteDTO> ingredientes = ingredienteService.listaIngredientes(idComida);
        if (ingredientes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comida no encontrada");
        }

        return ResponseEntity.ok(ingredientes);
    }

    @PutMapping("/{idIngrediente}/comidas/{idComida}")
    public ResponseEntity<?> editarIngrediente(@PathVariable Long idIngrediente, @PathVariable Long idComida, @RequestBody IngredienteDTO ingredienteDTO) {
        try {
            IngredienteDTO ingredienteEditado = ingredienteService.editarIngrediente(idComida, idIngrediente, ingredienteDTO);
            if (ingredienteEditado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingrediente no encontrado");
            }
            return ResponseEntity.ok(ingredienteEditado);
        } catch (IngredienteNoPerteneceADietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (CantidadNegativaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idIngrediente}/comidas/{idComida}")
    public ResponseEntity<?> borrarIngrediente(@PathVariable Long idIngrediente, @PathVariable Long idComida) {
        try {
            boolean eliminado = ingredienteService.borrarIngrediente(idIngrediente, idComida);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingrediente no encontrado");
            }
            return new ResponseEntity<>("Ingrediente borrado", HttpStatus.OK);
        } catch (IngredienteNoPerteneceADietaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

}
