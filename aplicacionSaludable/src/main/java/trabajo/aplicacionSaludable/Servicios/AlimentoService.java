package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.PropiedadesNegativasException;
import trabajo.aplicacionSaludable.Repositorios.AlimentoRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlimentoService {

    private final AlimentoRepository alimentoRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AlimentoService(AlimentoRepository alimentoRepository,  UsuarioRepository usuarioRepository) {
        this.alimentoRepository = alimentoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Alimento DTOaEntidad(AlimentoDTO alimentoDTO, Usuario usuario) {
        return new Alimento(
                alimentoDTO.getNombre(),
                alimentoDTO.getCalorias(),
                alimentoDTO.getProteinas(),
                alimentoDTO.getCarbohidratos(),
                alimentoDTO.getGrasas(),
                usuario
        );
    }

    private AlimentoDTO EntidadaDTO(Alimento alimento) {
        AlimentoDTO alimentoDTO = new AlimentoDTO();
        alimentoDTO.setIdAlimento(alimento.getIdAlimento());
        alimentoDTO.setNombre(alimento.getNombre());
        alimentoDTO.setCalorias(alimento.getCalorias());
        alimentoDTO.setProteinas(alimento.getProteinas());
        alimentoDTO.setCarbohidratos(alimento.getCarbohidratos());
        alimentoDTO.setGrasas(alimento.getGrasas());
        return alimentoDTO;
    }

    public AlimentoDTO creaAlimento(AlimentoDTO alimentoDTO, Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        if (alimentoRepository.findByNombreAndUsuario(alimentoDTO.getNombre(), usuario).isPresent()) {
            throw new AlimentoDuplicadoException();
        }

        if (alimentoDTO.getCalorias() < 0 || alimentoDTO.getCarbohidratos() < 0 || alimentoDTO.getProteinas() < 0 || alimentoDTO.getGrasas() < 0) {
            throw new PropiedadesNegativasException();
        }

        Alimento nuevoAlimento = DTOaEntidad(alimentoDTO, usuario);

        Alimento alimentoGuardado = alimentoRepository.save(nuevoAlimento);


        return EntidadaDTO(alimentoGuardado);
    }

    public AlimentoDTO actualizaAlimento(Long idAlimento, AlimentoDTO alimentoDTO, Long idUsuario) {

        Alimento alimentoExistente = alimentoRepository.findById(idAlimento)
                .orElse(null);

        if (alimentoExistente == null) {
            return null;
        }

        if (!alimentoExistente.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new AlimentoDeOtroUsuarioException();
        }

        // Compruebo que no existe un alimento con ese nombre creado por el usuario o en la bbdd de forma global.
        if (!alimentoExistente.getNombre().equals(alimentoDTO.getNombre())) {
            if (alimentoRepository.findByNombreAndUsuarioIsNull(alimentoDTO.getNombre()).isPresent()
                    || alimentoRepository.findByNombreAndUsuario(alimentoDTO.getNombre(), alimentoExistente.getUsuario()).isPresent()) {
                throw new AlimentoDuplicadoException();
            }
        }

        if (alimentoDTO.getCalorias() < 0 || alimentoDTO.getCarbohidratos() < 0 || alimentoDTO.getProteinas() < 0 || alimentoDTO.getGrasas() < 0) {
            throw new PropiedadesNegativasException();
        }

        alimentoExistente.setNombre(alimentoDTO.getNombre());
        alimentoExistente.setCalorias(alimentoDTO.getCalorias());
        alimentoExistente.setProteinas(alimentoDTO.getProteinas());
        alimentoExistente.setCarbohidratos(alimentoDTO.getCarbohidratos());
        alimentoExistente.setGrasas(alimentoDTO.getGrasas());

        Alimento alimentoActualizado = alimentoRepository.save(alimentoExistente);
        return EntidadaDTO(alimentoActualizado);
    }

    public List<AlimentoDTO> listaAlimentosUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        List<Alimento> alimentos = alimentoRepository.findByUsuarioOrUsuarioIsNull(usuario);

        return alimentos.stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public boolean borraAlimento(Long idAlimento, Long idUsuario) {

        Alimento alimentoExistente = alimentoRepository.findById(idAlimento)
                .orElse(null);

        if (alimentoExistente == null) {
            return false;
        }

        if (!alimentoExistente.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new AlimentoDeOtroUsuarioException();
        }

        alimentoRepository.deleteById(idAlimento);
        return true;
    }
}
