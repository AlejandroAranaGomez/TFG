package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.EjercicioEnDiaRutina;
import trabajo.aplicacionSaludable.Dominio.Serie;
import trabajo.aplicacionSaludable.Dtos.SerieDTO;

@Component
public class SerieAssembler {

    public Serie dtoAEntidad(SerieDTO dto, EjercicioEnDiaRutina ejercicioEnDiaRutina) {
        Serie serie = new Serie();

        serie.setIdSerie(dto.getIdSerie());
        serie.setSerieAnterior(dto.getSerieAnterior());
        serie.setRepeticiones(dto.getRepeticiones());
        serie.setPeso(dto.getPeso());
        serie.setEjercicioEnDiaRutina(ejercicioEnDiaRutina);

        return serie;
    }

    public SerieDTO entidadADTO(Serie serie) {
        return new SerieDTO(
                serie.getIdSerie(),
                serie.getSerieAnterior(),
                serie.getRepeticiones(),
                serie.getPeso()
        );
    }

}
