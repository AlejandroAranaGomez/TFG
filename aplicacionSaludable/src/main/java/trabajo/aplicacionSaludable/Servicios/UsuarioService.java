package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.InicioSesionDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.time.LocalDate;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder PasswordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder PasswordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.PasswordEncoder = PasswordEncoder;
    }

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) throws Exception {

        // Compruebo que no hay un usuario con ese email ya registrado.

        if (usuarioRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new Exception("Ya hay un usuario con este email.");
        }

        // Hasheo la contraseña para no pasarla directamente
        String contrasenhaHash = PasswordEncoder.encode(registroDTO.getContrasenha());

        Usuario nuevoUsuario = getUsuario(registroDTO, contrasenhaHash);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return new UsuarioDTO(
                usuarioGuardado.getIdUsuario(),
                usuarioGuardado.getNombre()
        );
    }

    //Metodo auxiliar que parsea la fecha y crea el usuario
    private static Usuario getUsuario(RegistroDTO registroDTO, String contrasenhaHash) {
        LocalDate fechaNacimiento = LocalDate.parse(registroDTO.getFechaNacimiento());

        // Creo el usuario a partir del dto y le guardo
        return new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellido1(),
                registroDTO.getApellido2(),
                fechaNacimiento,
                registroDTO.getGenero(),
                registroDTO.getPeso(),
                registroDTO.getAltura(),
                registroDTO.getEmail(),
                registroDTO.getTelefono(),
                contrasenhaHash,
                registroDTO.getObjetivo(),
                registroDTO.getNivelDeActividad()
        );
    }

    public UsuarioDTO iniciarSesion(InicioSesionDTO inicioSesionDTO) throws Exception {


        // Compruebo si existe un usuario con ese email.
       Usuario usuario = usuarioRepository.findByEmail(inicioSesionDTO.getEmail())
               .orElseThrow(() -> new Exception("No existe un usuario con este email."));

        // Compruebo si la contrasenha coincide con la que hemos escrito.
        if (!PasswordEncoder.matches(inicioSesionDTO.getContrasenha(), usuario.getContrasenha())) {
            throw new Exception("La contraseña no coincide.");
        }

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }

}
