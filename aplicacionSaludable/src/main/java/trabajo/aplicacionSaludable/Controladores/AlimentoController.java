package trabajo.aplicacionSaludable.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.PropiedadesNegativasException;
import trabajo.aplicacionSaludable.Servicios.AlimentoService;

import java.util.List;

// En estas clases reciben y gestionan las peticiones HTTP de los clientes.

@RestController
@RequestMapping("/api/usuarios/{idUsuario}/alimentos")
public class AlimentoController {

    private AlimentoService alimentoService;

    public AlimentoController(AlimentoService alimentoService) {
        this.alimentoService = alimentoService;
    }

    @PostMapping
    public ResponseEntity<?> crearAlimento(@PathVariable Long idUsuario, @RequestBody AlimentoDTO alimentoDTO) {
        try {
            AlimentoDTO nuevoAlimento = alimentoService.creaAlimento(alimentoDTO, idUsuario);
            if(nuevoAlimento == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            return new ResponseEntity<>(nuevoAlimento, HttpStatus.CREATED);
        } catch (AlimentoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (PropiedadesNegativasException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{idAlimento}")
    public ResponseEntity<?> actualizarAlimento(@PathVariable Long idAlimento, @PathVariable Long idUsuario, @RequestBody AlimentoDTO alimentoDTO) {
        try {
            AlimentoDTO alimentoActualizado = alimentoService.actualizaAlimento(idAlimento, alimentoDTO, idUsuario);
            if (alimentoActualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alimento no encontrado");
            }
            return ResponseEntity.ok(alimentoActualizado);
        } catch (AlimentoDuplicadoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (PropiedadesNegativasException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (AlimentoDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> obtenerAlimentosUsuario(@PathVariable Long idUsuario) {

            List<AlimentoDTO> listaAlimentos = alimentoService.listaAlimentosUsuario(idUsuario);
            if (listaAlimentos == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
            return ResponseEntity.ok(listaAlimentos);
    }

    @DeleteMapping("/{idAlimento}")
    public ResponseEntity<?> borrarAlimento(@PathVariable Long idAlimento, @PathVariable Long idUsuario) {
        try {
            boolean eliminado = alimentoService.borraAlimento(idAlimento, idUsuario);
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alimento no encontrado");
            }
            return ResponseEntity.ok("Alimento eliminado");
        } catch (AlimentoDeOtroUsuarioException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
