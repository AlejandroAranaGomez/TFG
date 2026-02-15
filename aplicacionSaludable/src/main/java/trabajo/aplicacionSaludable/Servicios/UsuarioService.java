package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Credenciales;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.InicioSesionDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioPerfilDTO;
import trabajo.aplicacionSaludable.Repositorios.CredencialesRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.time.LocalDate;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CredencialesRepository credencialesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
                          CredencialesRepository credencialesRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {

        // Comprobar si ya existe el email
        if (credencialesRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            return null;
        }

        Usuario usuario = crearUsuarioDesdeDTO(registroDTO);

        usuarioRepository.save(usuario);

        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }

    public UsuarioDTO iniciarSesion(InicioSesionDTO inicioSesionDTO) {

        Credenciales credenciales = credencialesRepository
                .findByEmail(inicioSesionDTO.getEmail())
                .orElse(null);

        if (credenciales == null) {
            return null;
        }

        if (!passwordEncoder.matches(
                inicioSesionDTO.getContrasenha(),
                credenciales.getContrasenha())) {
            return null;
        }

        Usuario usuario = credenciales.getUsuario();

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

        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido1(usuario.getApellido1());
        dto.setApellido2(usuario.getApellido2());
        dto.setFechaNacimiento(usuario.getFechaNacimiento().toString());
        dto.setGenero(usuario.getGenero());
        dto.setPeso(usuario.getPeso());
        dto.setAltura(usuario.getAltura());
        dto.setTelefono(usuario.getTelefono());
        dto.setObjetivo(usuario.getObjetivo());
        dto.setNivelDeActividad(usuario.getNivelDeActividad());
        dto.setEmail(usuario.getCredenciales().getEmail());

        return dto;
    }

    private Usuario crearUsuarioDesdeDTO(RegistroDTO registroDTO) {

        LocalDate fechaNacimiento = LocalDate.parse(registroDTO.getFechaNacimiento());

        Usuario usuario = new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellido1(),
                registroDTO.getApellido2(),
                fechaNacimiento,
                registroDTO.getGenero(),
                registroDTO.getPeso(),
                registroDTO.getAltura(),
                registroDTO.getTelefono(),
                registroDTO.getObjetivo(),
                registroDTO.getNivelDeActividad()
        );

        Credenciales credenciales = new Credenciales(
                registroDTO.getEmail(),
                passwordEncoder.encode(registroDTO.getContrasenha())
        );

        credenciales.setUsuario(usuario);
        usuario.setCredenciales(credenciales);

        return usuario;
    }

}
