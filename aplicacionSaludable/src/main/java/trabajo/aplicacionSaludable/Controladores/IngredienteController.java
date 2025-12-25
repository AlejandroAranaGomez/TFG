package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Servicios.IngredienteService;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @PostMapping("/comidas/{idComida}")
    public ResponseEntity<?> crearIngrediente(@PathVariable Long idComida, @RequestBody IngredienteDTO ingredienteDTO) throws Exception {
        try {
            IngredienteDTO ingredienteNuevo = ingredienteService.crearIngrediente(idComida, ingredienteDTO);
            return new ResponseEntity<>(ingredienteNuevo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/comidas/{idComida}")
    public ResponseEntity<?> obtenerIngredientes(@PathVariable Long idComida) throws Exception {
        try {
            List<IngredienteDTO> ingredientes = ingredienteService.listaIngredientes(idComida);
            return ResponseEntity.ok(ingredientes);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idIngrediente}/comidas/{idComida}")
    public ResponseEntity<?> editarIngrediente(@PathVariable Long idIngrediente, @PathVariable Long idComida, @RequestBody IngredienteDTO ingredienteDTO) {
        try {
            IngredienteDTO ingredienteEditado = ingredienteService.editarIngrediente(idComida, idIngrediente, ingredienteDTO);
            return ResponseEntity.ok(ingredienteEditado);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idIngrediente}/comidas/{idComida}")
    public ResponseEntity<?> borrarIngrediente(@PathVariable Long idIngrediente, @PathVariable Long idComida) {
        try {
            ingredienteService.borrarIngrediente(idIngrediente, idComida);
            return new ResponseEntity<>("Ingrediente borrado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
