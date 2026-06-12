package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.EjercicioEnDiaRutina;
import trabajo.aplicacionSaludable.Dominio.Serie;
import java.util.List;

@Repository
public interface SerieRepository extends JpaRepository<Serie, Long> {

    List<Serie> findByEjercicioEnDiaRutinaOrderByIdSerieAsc(EjercicioEnDiaRutina ejercicioEnDiaRutina);
}
