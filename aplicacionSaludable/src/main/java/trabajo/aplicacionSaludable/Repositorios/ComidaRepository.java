package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComidaRepository extends JpaRepository<Comida,Long> {

    List<Comida> findByDiaEnDieta(DiaEnDieta diaEnDieta);

    Optional<Comida> findByNombreAndDiaEnDieta(String nombre, DiaEnDieta diaEnDieta);
}
