package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
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
            return new ResponseEntity<>(nuevaComida, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/dias/{idDiaEnDieta}")
    public ResponseEntity<?> obtenerComidas(@PathVariable Long idDiaEnDieta) throws Exception {
        try {
            List<ComidaDTO> comidas = comidaService.listaComidas(idDiaEnDieta);
            return ResponseEntity.ok(comidas);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{idComida}/dias/{idDiaEnDieta}")
    public ResponseEntity<?> editarComida(@PathVariable Long idComida, @PathVariable Long idDiaEnDieta, @RequestBody ComidaDTO comidaDTO) {
        try {
            ComidaDTO comidaEditada = comidaService.editarComida(idDiaEnDieta, idComida, comidaDTO);
            return ResponseEntity.ok(comidaEditada);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idComida}/dias/{idDiaEnDieta}")
    public ResponseEntity<?> borrarComida(@PathVariable Long idComida, @PathVariable Long idDiaEnDieta) {
        try {
            comidaService.borrarComida(idDiaEnDieta, idComida);
            return new ResponseEntity<>("Comida borrada", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
