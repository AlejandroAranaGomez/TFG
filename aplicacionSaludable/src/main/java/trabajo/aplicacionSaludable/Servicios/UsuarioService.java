package trabajo.aplicacionSaludable.Servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Credenciales;
import trabajo.aplicacionSaludable.Dominio.HistorialPeso;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.*;
import trabajo.aplicacionSaludable.Repositorios.CredencialesRepository;
import trabajo.aplicacionSaludable.Repositorios.HistorialPesoRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CredencialesRepository credencialesRepository;
    private final HistorialPesoRepository historialPesoRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, CredencialesRepository credencialesRepository, HistorialPesoRepository historialPesoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.historialPesoRepository = historialPesoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {

        // Comprobar si ya existe el email
        if (credencialesRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            return null;
        }

        Usuario usuario = crearUsuarioDesdeDTO(registroDTO);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        HistorialPeso historial = new HistorialPeso();
        historial.setPeso(usuarioGuardado.getPeso());
        historial.setFecha(LocalDate.now());
        historial.setUsuario(usuarioGuardado);

        historialPesoRepository.save(historial);

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

    public UsuarioPerfilDTO actualizarPerfil(Long idUsuario, UsuarioActualizarDTO dto) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido1(dto.getApellido1());
        usuario.setApellido2(dto.getApellido2());
        usuario.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        usuario.setAltura(dto.getAltura());
        usuario.setTelefono(dto.getTelefono());
        usuario.setObjetivo(dto.getObjetivo());
        usuario.setNivelDeActividad(dto.getNivelDeActividad());

        int calorias = CalculoCalorias.calcularCalorias(
                usuario.getPeso(),
                usuario.getAltura(),
                usuario.getFechaNacimiento(),
                usuario.getGenero(),
                usuario.getNivelDeActividad(),
                usuario.getObjetivo()
        );

        usuario.setCaloriasObjetivo(calorias);

        usuarioRepository.save(usuario);

        return obtenerPerfilUsuario(usuario.getIdUsuario());
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
        dto.setCaloriasObjetivo(usuario.getCaloriasObjetivo());

        return dto;
    }

    public UsuarioPerfilDTO actualizarPeso(Long idUsuario, float nuevoPeso) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        if (usuario.getPeso() != nuevoPeso) {

            usuario.setPeso(nuevoPeso);

            int calorias = CalculoCalorias.calcularCalorias(
                    usuario.getPeso(),
                    usuario.getAltura(),
                    usuario.getFechaNacimiento(),
                    usuario.getGenero(),
                    usuario.getNivelDeActividad(),
                    usuario.getObjetivo()
            );

            usuario.setCaloriasObjetivo(calorias);

            usuarioRepository.save(usuario);

            HistorialPeso historial = new HistorialPeso();
            historial.setPeso(nuevoPeso);
            historial.setFecha(LocalDate.now());
            historial.setUsuario(usuario);

            historialPesoRepository.save(historial);
        }

        return obtenerPerfilUsuario(idUsuario);
    }

    public List<HistorialPesoDTO> obtenerHistorialPeso(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        List<HistorialPeso> historial = historialPesoRepository.findByUsuarioIdUsuarioOrderByFechaAsc(idUsuario);

        return historial.stream().map(h -> new HistorialPesoDTO(h.getPeso(), h.getFecha().toString())).toList();
    }

    private Usuario crearUsuarioDesdeDTO(RegistroDTO registroDTO) {

        LocalDate fechaNacimiento = LocalDate.parse(registroDTO.getFechaNacimiento());

        int calorias = CalculoCalorias.calcularCalorias(
                registroDTO.getPeso(),
                registroDTO.getAltura(),
                fechaNacimiento,
                registroDTO.getGenero(),
                registroDTO.getNivelDeActividad(),
                registroDTO.getObjetivo()
        );

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
                registroDTO.getNivelDeActividad(),
                calorias
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
