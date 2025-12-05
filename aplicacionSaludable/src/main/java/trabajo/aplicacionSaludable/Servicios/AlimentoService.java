package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;
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

    public AlimentoDTO creaAlimento(AlimentoDTO alimentoDTO, Long idUsuario) throws Exception {

        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("No existe un usuario con este id."));

        if (alimentoRepository.findByNombreAndUsuario(alimentoDTO.getNombre(), usuario).isPresent()) {
            throw new Exception("Ya tienes un alimento creado con este nombre.");
        }

        if (alimentoRepository.findByNombreAndUsuarioIsNull(alimentoDTO.getNombre()).isPresent()) {
            throw new Exception("Ya existe un alimento con este nombre en la aplicacion.");
        }

        if (alimentoDTO.getCalorias() < 0 || alimentoDTO.getCarbohidratos() < 0 || alimentoDTO.getProteinas() < 0 || alimentoDTO.getGrasas() < 0) {
            throw new Exception("Las propiedades de los alimentos no pueden ser negativas.");
        }

        Alimento nuevoAlimento = DTOaEntidad(alimentoDTO, usuario);

        Alimento alimentoGuardado = alimentoRepository.save(nuevoAlimento);


        return EntidadaDTO(alimentoGuardado);
    }

    public AlimentoDTO actualizaAlimento(Long idAlimento, AlimentoDTO alimentoDTO, Long idUsuario) throws Exception {

        Alimento alimentoExistente = alimentoRepository.findById(idAlimento)
                .orElseThrow(() -> new Exception("No existe un alimento con este id."));

        if (alimentoExistente.getUsuario() == null) {
            throw new Exception("No puedes editar un alimento global.");
        }

        if (!alimentoExistente.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new Exception("Este alimento pertenece a otro usuario.");
        }

        // Compruebo que no existe un alimento con ese nombre creado por el usuario o en la bbdd de forma global.
        if (!alimentoExistente.getNombre().equals(alimentoDTO.getNombre())) {
            if (alimentoRepository.findByNombreAndUsuarioIsNull(alimentoDTO.getNombre()).isPresent()
                    || alimentoRepository.findByNombreAndUsuario(alimentoDTO.getNombre(), alimentoExistente.getUsuario()).isPresent()) {
                throw new Exception("Ya existe un alimento con este nombre.");
            }
        }

        alimentoExistente.setNombre(alimentoDTO.getNombre());
        alimentoExistente.setCalorias(alimentoDTO.getCalorias());
        alimentoExistente.setProteinas(alimentoDTO.getProteinas());
        alimentoExistente.setCarbohidratos(alimentoDTO.getCarbohidratos());
        alimentoExistente.setGrasas(alimentoDTO.getGrasas());

        Alimento alimentoActualizado = alimentoRepository.save(alimentoExistente);
        return EntidadaDTO(alimentoActualizado);
    }

    public List<AlimentoDTO> listaAlimentosUsuario(Long idUsuario) throws Exception {
        Usuario usuario = usuarioRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new Exception("No existe un usuario con este id."));

        List<Alimento> alimentos = alimentoRepository.findByUsuarioOrUsuarioIsNull(usuario);

        return alimentos.stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public List<AlimentoDTO> listaAlimentosGlobales() throws Exception {

        List<Alimento> alimentos = alimentoRepository.findByUsuarioIsNull();

        return alimentos.stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public void borraAlimento(Long idAlimento, Long idUsuario) throws Exception {

        Alimento alimentoExistente = alimentoRepository.findById(idAlimento)
                .orElseThrow(() -> new Exception("No existe un alimento con este id."));

        if (alimentoExistente.getUsuario() == null) {
            throw new Exception("No puedes borrar un alimento global.");
        }

        if (!alimentoExistente.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new Exception("Este alimento pertenece a otro usuario.");
        }

        alimentoRepository.deleteById(idAlimento);
    }



}
