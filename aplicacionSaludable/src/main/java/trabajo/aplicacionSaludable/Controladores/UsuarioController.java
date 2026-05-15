package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dominio.HistorialPeso;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.HistorialPesoDTO;
import trabajo.aplicacionSaludable.Dtos.PesoDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioActualizarDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioPerfilDTO;
import trabajo.aplicacionSaludable.Servicios.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long idUsuario) {

        UsuarioPerfilDTO usuarioPerfilDTO = usuarioService.obtenerPerfilUsuario(idUsuario);

        if (usuarioPerfilDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(usuarioPerfilDTO);

    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long idUsuario, @RequestBody UsuarioActualizarDTO dto) {

        UsuarioPerfilDTO usuarioPerfilDTO = usuarioService.actualizarPerfil(idUsuario, dto);

        if (usuarioPerfilDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(usuarioPerfilDTO);
    }

    @GetMapping("/{idUsuario}/historialPeso")
    public ResponseEntity<?> obtenerHistorialPeso(@PathVariable Long idUsuario) {

        List<HistorialPesoDTO> historial = usuarioService.obtenerHistorialPeso(idUsuario);

        if (historial == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(historial);
    }

    @PostMapping("/{idUsuario}/historialPeso")
    public ResponseEntity<?> actualizarPeso(@PathVariable Long idUsuario, @RequestBody PesoDTO nuevoPeso) {

        UsuarioPerfilDTO usuario = usuarioService.actualizarPeso(idUsuario, nuevoPeso.getPeso());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(usuario);

    }
}

