package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;
import trabajo.aplicacionSaludable.Servicios.AlimentoService;

import java.util.List;

@RestController
@RequestMapping("/api/alimentos")
public class AlimentoController {

    @Autowired
    private AlimentoService alimentoService;

    @PostMapping("usuarios/{idUsuario}")
    public ResponseEntity<?> crearAlimento(@PathVariable Long idUsuario, @RequestBody AlimentoDTO alimentoDTO) throws Exception {
        try {
            AlimentoDTO nuevoAlimento = alimentoService.creaAlimento(alimentoDTO, idUsuario);
            return new ResponseEntity<>(nuevoAlimento, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{idAlimento}/usuarios/{idUsuario}")
    public ResponseEntity<?> actualizarAlimento(@PathVariable Long idAlimento, @PathVariable Long idUsuario, @RequestBody AlimentoDTO alimentoDTO) throws Exception {
        try {
            AlimentoDTO alimentoActualizado = alimentoService.actualizaAlimento(idAlimento, alimentoDTO, idUsuario);
            return ResponseEntity.ok(alimentoActualizado);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuarios/{idUsuario}")
    public ResponseEntity<?> obtenerAlimentosUsuario(@PathVariable Long idUsuario) throws Exception {
        try {
            List<AlimentoDTO> listaAlimentos = alimentoService.listaAlimentosUsuario(idUsuario);
            return ResponseEntity.ok(listaAlimentos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/globales")
    public ResponseEntity<?> obtenerAlimentosGlobales() throws Exception {
        try {
            List<AlimentoDTO> listaAlimentos = alimentoService.listaAlimentosGlobales();
            return ResponseEntity.ok(listaAlimentos);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idAlimento}/usuarios/{idUsuario}")
    public ResponseEntity<?> borrarAlimento(@PathVariable Long idAlimento, @PathVariable Long idUsuario) throws Exception {
        try {
            alimentoService.borraAlimento(idAlimento, idUsuario);
            return new ResponseEntity<>("Alimento borrado",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
