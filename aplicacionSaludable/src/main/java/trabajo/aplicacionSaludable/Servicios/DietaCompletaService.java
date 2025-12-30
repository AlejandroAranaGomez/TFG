package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas.DietaDuplicadaException;
import trabajo.aplicacionSaludable.Repositorios.DietaCompletaRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietaCompletaService {

    private DietaCompletaRepository dietaCompletaRepository;
    private UsuarioRepository usuarioRepository;

    @Autowired
    public DietaCompletaService(DietaCompletaRepository dietaCompletaRepository, UsuarioRepository usuarioRepository) {
        this.dietaCompletaRepository = dietaCompletaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private DietaCompleta DTOaEntidad(DietaCompletaDTO dietaCompletaDTO, Usuario usuario) {
        DietaCompleta dieta = new DietaCompleta();
        dieta.setNombre(dietaCompletaDTO.getNombre());
        dieta.setDescripcion(dietaCompletaDTO.getDescripcion());
        dieta.setCaloriasTotales(dietaCompletaDTO.getCaloriasTotales());
        dieta.setProteinas(dietaCompletaDTO.getProteinas());
        dieta.setCarbohidratos(dietaCompletaDTO.getCarbohidratos());
        dieta.setGrasas(dietaCompletaDTO.getGrasas());
        dieta.setActiva(dietaCompletaDTO.isActiva());
        dieta.setUsuario(usuario);
        return dieta;
    }

    private DietaCompletaDTO EntidadaDTO(DietaCompleta dietaCompleta) {
        DietaCompletaDTO dietaCompletaDTO = new DietaCompletaDTO();
        dietaCompletaDTO.setIdDietaCompleta(dietaCompleta.getIdDietaCompleta());
        dietaCompletaDTO.setNombre(dietaCompleta.getNombre());
        dietaCompletaDTO.setDescripcion(dietaCompleta.getDescripcion());
        dietaCompletaDTO.setCaloriasTotales(dietaCompleta.getCaloriasTotales());
        dietaCompletaDTO.setProteinas(dietaCompleta.getProteinas());
        dietaCompletaDTO.setCarbohidratos(dietaCompleta.getCarbohidratos());
        dietaCompletaDTO.setGrasas(dietaCompleta.getGrasas());
        dietaCompletaDTO.setActiva(dietaCompleta.isActiva());
        return dietaCompletaDTO;
    }

    private void desactivarDieta(Usuario usuario) {
        Optional<DietaCompleta> dietaActiva = dietaCompletaRepository.findByUsuarioAndActivaTrue(usuario);

        if (dietaActiva.isPresent()) {
            DietaCompleta antigua = dietaActiva.get();
            antigua.setActiva(false);
            dietaCompletaRepository.save(antigua);
        }
    }

    public DietaCompletaDTO creaDietaCompleta(Long idUsuario, DietaCompletaDTO dietaCompletaDTO) {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        if (dietaCompletaRepository.findByNombreAndUsuario(dietaCompletaDTO.getNombre(), usuario).isPresent()) {
            throw new DietaDuplicadaException();
        }

        if (dietaCompletaDTO.isActiva()) {
            desactivarDieta(usuario);
        }

        DietaCompleta nuevaDietaCompleta = DTOaEntidad(dietaCompletaDTO, usuario);
        DietaCompleta dietaGuardada = dietaCompletaRepository.save(nuevaDietaCompleta);

        return EntidadaDTO(dietaGuardada);
    }

    public DietaCompletaDTO actualizaDietaCompleta(DietaCompletaDTO dietaCompletaDTO, Long idUsuario, Long idDietaCompleta) {
        DietaCompleta dietaExistente = dietaCompletaRepository.findById(idDietaCompleta)
                .orElse(null);

        if (dietaExistente == null) {
            return null;
        }


        if (!dietaExistente.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new DietaDeOtroUsuarioException();
        }

        Optional<DietaCompleta> otraDietaMismoNombre = dietaCompletaRepository.findByNombreAndUsuario(dietaCompletaDTO.getNombre(), dietaExistente.getUsuario());
        if (otraDietaMismoNombre.isPresent() && !otraDietaMismoNombre.get().getIdDietaCompleta().equals(idDietaCompleta)) {
            throw new DietaDuplicadaException();
        }

        if (dietaCompletaDTO.isActiva() && !dietaExistente.isActiva()) {
            desactivarDieta(dietaExistente.getUsuario());
        }

        dietaExistente.setNombre(dietaCompletaDTO.getNombre());
        dietaExistente.setDescripcion(dietaCompletaDTO.getDescripcion());
        dietaExistente.setActiva(dietaCompletaDTO.isActiva());

        DietaCompleta dietaActualizada = dietaCompletaRepository.save(dietaExistente);
        return EntidadaDTO(dietaActualizada);
    }

    public boolean borraDietaCompleta(Long idUsuario, Long idDietaCompleta) {
        DietaCompleta dietaCompleta = dietaCompletaRepository.findById(idDietaCompleta).orElse(null);

        if (dietaCompleta == null) {
            return false;
        }

        if (!dietaCompleta.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new DietaDeOtroUsuarioException();
        }

        dietaCompletaRepository.deleteById(idDietaCompleta);
        return true;
    }

    public List<DietaCompletaDTO> listaDietaCompletaUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        return dietaCompletaRepository.findByUsuario(usuario).stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

}
