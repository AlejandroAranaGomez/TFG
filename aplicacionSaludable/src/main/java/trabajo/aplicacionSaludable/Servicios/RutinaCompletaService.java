package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
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

    private RutinaCompletaRepository rutinaCompletaRepository;
    private UsuarioRepository usuarioRepository;

    public RutinaCompletaService(RutinaCompletaRepository rutinaCompletaRepository, UsuarioRepository usuarioRepository) {
        this.rutinaCompletaRepository = rutinaCompletaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private RutinaCompleta DTOaEntidad(RutinaCompletaDTO dto, Usuario usuario) {
        RutinaCompleta rutina = new RutinaCompleta();
        rutina.setIdRutinaCompleta(dto.getIdRutinaCompleta());
        rutina.setNombreRutinaCompleta(dto.getNombreRutinaCompleta());
        rutina.setResumen(dto.getResumen());
        rutina.setUsuario(usuario);
        return rutina;
    }

    private RutinaCompletaDTO EntidadaDTO(RutinaCompleta rutina) {
        RutinaCompletaDTO dto = new RutinaCompletaDTO();
        dto.setIdRutinaCompleta(rutina.getIdRutinaCompleta());
        dto.setResumen(rutina.getResumen());
        dto.setNombreRutinaCompleta(rutina.getNombreRutinaCompleta());
        return dto;
    }

    private DiaEnRutinaDTO convertirDiaADTO(DiaEnRutina dia) {
        DiaEnRutinaDTO dto = new DiaEnRutinaDTO();
        dto.setIdDiaEnRutina(dia.getIdDiaEnRutina());
        dto.setNombre(dia.getNombre());
        dto.setDiaDeLaSemana(dia.getDiaDeLaSemana());
        return dto;
    }

    public List<RutinaCompletaDTO> listarRutinasUsuario(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        return rutinaCompletaRepository.findByUsuario(usuario).stream().map(this::EntidadaDTO).collect(Collectors.toList());
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

        RutinaCompleta rutina = DTOaEntidad(rutinaCompletaDTO, usuario);
        RutinaCompleta guardada = rutinaCompletaRepository.save(rutina);

        return EntidadaDTO(guardada);

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
        return EntidadaDTO(actualizada);
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

        RutinaCompletaDTO dto = EntidadaDTO(rutinaExiste);

        List<DiaEnRutinaDTO > dias = rutinaExiste.getDiaEnRutinas()
                .stream()
                .map(this::convertirDiaADTO)
                .collect(Collectors.toList());

        dto.setDias(dias);

        return dto;
    }
}
