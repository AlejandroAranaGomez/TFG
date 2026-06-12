package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.RutinaCompletaAssembler;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.RutinaCompletaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas.RutinaDuplicadaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas.RutinaOtroUsuarioException;
import trabajo.aplicacionSaludable.Repositorios.RutinaCompletaRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RutinaCompletaService {

    private final RutinaCompletaRepository rutinaCompletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RutinaCompletaAssembler rutinaCompletaAssembler;

    public RutinaCompletaService(RutinaCompletaRepository rutinaCompletaRepository, RutinaCompletaAssembler rutinaCompletaAssembler, UsuarioRepository usuarioRepository) {
        this.rutinaCompletaRepository = rutinaCompletaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rutinaCompletaAssembler = rutinaCompletaAssembler;
    }

    public List<RutinaCompletaDTO> listarRutinasUsuario(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        return rutinaCompletaRepository.findByUsuario(usuario).stream().map(rutinaCompletaAssembler::entidadADTO).collect(Collectors.toList());
    }

    public RutinaCompletaDTO crearRutina(Long idUsuario, RutinaCompletaDTO rutinaCompletaDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        if(rutinaCompletaRepository.findByNombreRutinaCompletaAndUsuario(rutinaCompletaDTO.getNombreRutinaCompleta(), usuario).isPresent()) {
            throw new RutinaDuplicadaException();
        }

        RutinaCompleta rutina = rutinaCompletaAssembler.dtoAEntidad(rutinaCompletaDTO, usuario);
        RutinaCompleta guardada = rutinaCompletaRepository.save(rutina);

        return rutinaCompletaAssembler.entidadADTO(guardada);

    }

    public RutinaCompletaDTO actualizarRutina(Long idUsuario, Long idRutina, RutinaCompletaDTO rutinaCompletaDTO) {
        RutinaCompleta rutinaExiste = rutinaCompletaRepository.findById(idRutina).orElse(null);

        if (rutinaExiste == null) {
            return null;
        }

        if (!rutinaExiste.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RutinaOtroUsuarioException();
        }

        Optional<RutinaCompleta> otraRutina = rutinaCompletaRepository
                .findByNombreRutinaCompletaAndUsuario(rutinaCompletaDTO.getNombreRutinaCompleta(), rutinaExiste.getUsuario());

        if (otraRutina.isPresent() && !otraRutina.get().getIdRutinaCompleta().equals(idRutina)) {
            throw new RutinaDuplicadaException();
        }

        rutinaExiste.setNombreRutinaCompleta(rutinaCompletaDTO.getNombreRutinaCompleta());
        rutinaExiste.setResumen(rutinaCompletaDTO.getResumen());

        RutinaCompleta actualizada = rutinaCompletaRepository.save(rutinaExiste);
        return rutinaCompletaAssembler.entidadADTO(actualizada);
    }

    public boolean borrarRutina(Long idUsuario, Long idRutina) {
        RutinaCompleta rutinaExiste = rutinaCompletaRepository.findById(idRutina).orElse(null);

        if (rutinaExiste == null) {
            return false;
        }

        if (!rutinaExiste.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RutinaOtroUsuarioException();
        }

        rutinaCompletaRepository.deleteById(idRutina);
        return true;
    }

    public RutinaCompletaDTO obtenerRutina(Long idUsuario, Long idRutina) {
        RutinaCompleta rutinaExiste = rutinaCompletaRepository.findById(idRutina).orElse(null);

        if (rutinaExiste == null) {
            return null;
        }

        if (!rutinaExiste.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RutinaOtroUsuarioException();
        }

        return rutinaCompletaAssembler.entidadADTOConDias(rutinaExiste);
    }
}
