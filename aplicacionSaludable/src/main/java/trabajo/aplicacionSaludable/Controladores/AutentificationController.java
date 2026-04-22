package trabajo.aplicacionSaludable.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.InicioSesionDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Servicios.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class AutentificationController {

    private UsuarioService usuarioService;

    public AutentificationController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO) {

        UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroDTO);

        if (usuarioDTO == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email ya está registrado");
        }

        return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody InicioSesionDTO inicioSesionDTO) {
        UsuarioDTO usuarioDTO = usuarioService.iniciarSesion(inicioSesionDTO);

        if (usuarioDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Credenciales incorrectas");
        }

        return ResponseEntity.ok(usuarioDTO);

    }

}
