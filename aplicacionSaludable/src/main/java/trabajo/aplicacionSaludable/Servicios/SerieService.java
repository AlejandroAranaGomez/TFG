package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.SerieAssembler;
import trabajo.aplicacionSaludable.Dominio.EjercicioEnDiaRutina;
import trabajo.aplicacionSaludable.Dominio.Serie;
import trabajo.aplicacionSaludable.Dtos.SerieDTO;
import trabajo.aplicacionSaludable.Repositorios.EjercicioEnDiaRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.SerieRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {
    private final SerieRepository serieRepository;
    private final EjercicioEnDiaRutinaRepository ejercicioEnDiaRutinaRepository;
    private final SerieAssembler serieAssembler;

    public SerieService(SerieRepository serieRepository, EjercicioEnDiaRutinaRepository ejercicioEnDiaRutinaRepository, SerieAssembler serieAssembler) {
        this.serieRepository = serieRepository;
        this.ejercicioEnDiaRutinaRepository = ejercicioEnDiaRutinaRepository;
        this.serieAssembler = serieAssembler;
    }

    public List<SerieDTO> listaSeries(Long idEjercicioEnDiaRutina) {
        EjercicioEnDiaRutina ejercicioEnDiaRutina = ejercicioEnDiaRutinaRepository.findById(idEjercicioEnDiaRutina).orElse(null);

        if (ejercicioEnDiaRutina == null) {
            return null;
        }

        return serieRepository.findByEjercicioEnDiaRutinaOrderByIdSerieAsc(ejercicioEnDiaRutina).stream().map(serieAssembler::entidadADTO).collect(Collectors.toList());
    }

    public SerieDTO crearSerie(Long idEjercicioEnDiaRutina, SerieDTO serieDTO) {
        EjercicioEnDiaRutina ejercicioEnDiaRutina = ejercicioEnDiaRutinaRepository.findById(idEjercicioEnDiaRutina).orElse(null);

        if (ejercicioEnDiaRutina == null) {
            return null;
        }

        Serie serie = serieAssembler.dtoAEntidad(serieDTO, ejercicioEnDiaRutina);
        Serie guardada = serieRepository.save(serie);

        return serieAssembler.entidadADTO(guardada);
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

        return serieAssembler.entidadADTO(actualizada);
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
