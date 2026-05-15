package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import trabajo.aplicacionSaludable.ApisExternas.ApiAlimentosResponse;
import trabajo.aplicacionSaludable.ApisExternas.ApiFood;
import trabajo.aplicacionSaludable.ApisExternas.ApiHints;
import trabajo.aplicacionSaludable.Assemblers.AlimentoAssembler;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;
import trabajo.aplicacionSaludable.Dtos.ApiAlimentosDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDeOtroUsuarioException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.AlimentoDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos.PropiedadesNegativasException;
import trabajo.aplicacionSaludable.Repositorios.AlimentoRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlimentoService {

    private static String URLAlimentos = "https://api.edamam.com/api/food-database/v2/parser";

    @Value("${edamam.app-id}")
    private String appId;

    @Value("${edamam.app-key}")
    private String appKey;

    private final RestTemplate restTemplate;
    private final AlimentoRepository alimentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlimentoAssembler alimentoAssembler;

    public AlimentoService(AlimentoRepository alimentoRepository,  UsuarioRepository usuarioRepository, AlimentoAssembler alimentoAssembler, RestTemplate restTemplate) {
        this.alimentoRepository = alimentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alimentoAssembler = alimentoAssembler;
        this.restTemplate = restTemplate;
    }

    public List<ApiAlimentosDTO> buscarAlimentos(String alimento) {

        String url =
                URLAlimentos
                        + "?app_id=" + appId
                        + "&app_key=" + appKey
                        + "&ingr=" + alimento;

        ApiAlimentosResponse response =
                restTemplate.getForObject(url, ApiAlimentosResponse.class);

        List<ApiAlimentosDTO> resultado = new ArrayList<>();

        if (response == null || response.getHints() == null) {
            return resultado;
        }

        for (ApiHints hint : response.getHints()) {
            ApiFood food = hint.getFood();
            if (food == null || food.getNutrients() == null) continue;

            ApiAlimentosDTO dto = new ApiAlimentosDTO();
            dto.setIdApi(food.getFoodId());
            dto.setNombre(food.getLabel());
            dto.setCalorias(food.getNutrients().getCalorias().floatValue());
            dto.setProteinas(food.getNutrients().getProteinas().floatValue());
            dto.setCarbohidratos(food.getNutrients().getCarbohidratos().floatValue());
            dto.setGrasas(food.getNutrients().getGrasas().floatValue());

            resultado.add(dto);
        }

        return resultado;
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

        Alimento nuevoAlimento = alimentoAssembler.dtoAEntidad(alimentoDTO, usuario);

        Alimento alimentoGuardado = alimentoRepository.save(nuevoAlimento);


        return alimentoAssembler.entidadADTO(alimentoGuardado);
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

        // Compruebo que no existe un alimento con ese nombre creado por el usuario
        if (!alimentoExistente.getNombre().equals(alimentoDTO.getNombre())) {
            if (alimentoRepository.findByNombreAndUsuario(alimentoDTO.getNombre(), alimentoExistente.getUsuario()).isPresent()) {
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
        return alimentoAssembler.entidadADTO(alimentoActualizado);
    }

    public List<AlimentoDTO> listaAlimentosUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElse(null);

        if (usuario == null) {
            return null;
        }

        List<Alimento> alimentos = alimentoRepository.findByUsuario(usuario);

        return alimentos.stream().map(alimentoAssembler::entidadADTO).collect(Collectors.toList());
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
