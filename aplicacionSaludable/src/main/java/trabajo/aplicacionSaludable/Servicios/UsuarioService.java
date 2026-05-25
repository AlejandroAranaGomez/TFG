package trabajo.aplicacionSaludable.Servicios;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.UsuarioAssembler;
import trabajo.aplicacionSaludable.Dominio.Credenciales;
import trabajo.aplicacionSaludable.Dominio.HistorialPeso;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.*;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesUsuarios.UsuarioYaRegistradoException;
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
    private final UsuarioAssembler usuarioAssembler;

    public UsuarioService(UsuarioRepository usuarioRepository, CredencialesRepository credencialesRepository, HistorialPesoRepository historialPesoRepository, PasswordEncoder passwordEncoder, UsuarioAssembler usuarioAssembler) {
        this.usuarioRepository = usuarioRepository;
        this.credencialesRepository = credencialesRepository;
        this.historialPesoRepository = historialPesoRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAssembler = usuarioAssembler;
    }

    public UsuarioDTO registrarUsuario(RegistroDTO registroDTO) {

        // Comprobar si ya existe el email
        if (credencialesRepository.findByEmail(registroDTO.getEmail()).isPresent()) {
            throw new UsuarioYaRegistradoException();
        }

        Usuario usuario = usuarioAssembler.dtoAEntidadRegistro(registroDTO);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        HistorialPeso historial = new HistorialPeso();
        historial.setPeso(usuarioGuardado.getPeso());
        historial.setFecha(LocalDate.now());
        historial.setUsuario(usuarioGuardado);

        historialPesoRepository.save(historial);

        return usuarioAssembler.entidadADTOBasico(usuarioGuardado);
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

        return usuarioAssembler.entidadADTOBasico(usuario);
    }

    public UsuarioPerfilDTO actualizarPerfil(Long idUsuario, UsuarioActualizarDTO dto) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        usuarioAssembler.actualizarEntidadDesdeDTO(usuario, dto);

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

        return usuarioAssembler.entidadADTOPerfil(usuario);
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

}
