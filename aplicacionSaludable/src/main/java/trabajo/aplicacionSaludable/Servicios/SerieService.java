package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;
import trabajo.aplicacionSaludable.Dominio.Serie;
import trabajo.aplicacionSaludable.Dtos.SerieDTO;
import trabajo.aplicacionSaludable.Repositorios.EjercicioRepository;
import trabajo.aplicacionSaludable.Repositorios.SerieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {
    private final SerieRepository serieRepository;
    private final EjercicioRepository ejercicioRepository;

    public SerieService(SerieRepository serieRepository, EjercicioRepository ejercicioRepository) {
        this.serieRepository = serieRepository;
        this.ejercicioRepository = ejercicioRepository;
    }

    private Serie DTOaEntidad(SerieDTO dto, Ejercicio ejercicio) {
        Serie serie = new Serie();
        serie.setIdSerie(dto.getIdSerie());
        serie.setSerieAnterior(dto.getSerieAnterior());
        serie.setRepeticiones(dto.getRepeticiones());
        serie.setPeso(dto.getPeso());
        serie.setEjercicio(ejercicio);
        return serie;
    }

    private SerieDTO EntidadaDTO(Serie serie) {
        return new SerieDTO(
                serie.getIdSerie(),
                serie.getSerieAnterior(),
                serie.getRepeticiones(),
                serie.getPeso()
        );
    }

    public List<SerieDTO> listaSeries(Long idEjercicio) {
        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio).orElse(null);

        if (ejercicio == null) {
            return null;
        }

        return  serieRepository.findByEjercicioOrderByIdSerieAsc(ejercicio).stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public SerieDTO crearSerie(Long idEjercicio, SerieDTO serieDTO) {
        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio).orElse(null);

        if (ejercicio == null) {
            return null;
        }

        Serie serie = DTOaEntidad(serieDTO, ejercicio);
        Serie guardada = serieRepository.save(serie);

        return EntidadaDTO(guardada);
    }

    public SerieDTO actualizarSerie(Long idSerie, SerieDTO serieDTO) {
        Serie serie = serieRepository.findById(idSerie).orElse(null);

        if (serie == null) {
            return null;
        }

        if (serie.getPeso() > 0 && serie.getRepeticiones() > 0) {
            String serieAnterior = serie.getPeso() + "kg x " + serie.getRepeticiones();
            serie.setSerieAnterior(serieAnterior);
        }

        serie.setPeso(serieDTO.getPeso());
        serie.setRepeticiones(serieDTO.getRepeticiones());
        Serie actualizada = serieRepository.save(serie);

        return EntidadaDTO(actualizada);
    }

    public  boolean borrarSerie(Long idSerie) {
        Serie serie = serieRepository.findById(idSerie).orElse(null);

        if (serie == null) {
            return false;
        }

        serieRepository.deleteById(idSerie);
        return true;
    }
}
