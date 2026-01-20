package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.InicioSesionDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioPerfilDTO;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.time.LocalDate;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {

        // Compruebo que no hay un usuario con ese email ya registrado.

        if (usuarioRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            return null;
        }

        // Hasheo la contraseña para no pasarla directamente
        String contrasenhaHash = passwordEncoder.encode(registroDTO.getContrasenha());

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

    public UsuarioDTO iniciarSesion(InicioSesionDTO inicioSesionDTO) {


        // Compruebo si existe un usuario con ese email.
       Usuario usuario = usuarioRepository.findByEmail(inicioSesionDTO.getEmail())
               .orElse(null);

       // No existe el usuario
        if (usuario == null) {
            return null;
        }

        // Compruebo si la contrasenha coincide con la que hemos escrito.
        if (!passwordEncoder.matches(inicioSesionDTO.getContrasenha(), usuario.getContrasenha())) {
            return null;
        }

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }

    public UsuarioPerfilDTO obtenerPerfilUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        UsuarioPerfilDTO usuarioPerfilDTO = new UsuarioPerfilDTO();
        usuarioPerfilDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioPerfilDTO.setNombre(usuario.getNombre());
        usuarioPerfilDTO.setApellido1(usuario.getApellido1());
        usuarioPerfilDTO.setApellido2(usuario.getApellido2());

        usuarioPerfilDTO.setFechaNacimiento(usuario.getFechaNacimiento().toString());


        usuarioPerfilDTO.setPeso(usuario.getPeso());
        usuarioPerfilDTO.setAltura(usuario.getAltura());
        usuarioPerfilDTO.setObjetivo(usuario.getObjetivo());
        usuarioPerfilDTO.setNivelDeActividad(usuario.getNivelDeActividad());

        usuarioPerfilDTO.setEmail(usuario.getEmail());
        usuarioPerfilDTO.setTelefono(usuario.getTelefono());
        usuarioPerfilDTO.setGenero(usuario.getGenero());

        return usuarioPerfilDTO;
    }

}
