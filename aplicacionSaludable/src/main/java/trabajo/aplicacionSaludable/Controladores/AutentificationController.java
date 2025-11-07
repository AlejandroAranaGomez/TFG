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
@RequestMapping("/api/autentification")
public class AutentificationController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.registrarUsuario(registroDTO);

            return new ResponseEntity<>(usuarioDTO, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody InicioSesionDTO inicioSesionDTO) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.iniciarSesion(inicioSesionDTO);

            return ResponseEntity.ok(usuarioDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

}
    