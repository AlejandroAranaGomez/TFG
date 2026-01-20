package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trabajo.aplicacionSaludable.Dtos.UsuarioPerfilDTO;
import trabajo.aplicacionSaludable.Servicios.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{idUsuario}/perfil")
    public ResponseEntity<?> obtenerPerfil(@PathVariable Long idUsuario) {

        UsuarioPerfilDTO usuarioPerfilDTO = usuarioService.obtenerPerfilUsuario(idUsuario);

        if (usuarioPerfilDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        return ResponseEntity.ok(usuarioPerfilDTO);

    }
}
