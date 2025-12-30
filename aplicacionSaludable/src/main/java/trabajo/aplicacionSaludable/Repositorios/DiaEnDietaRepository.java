package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiaEnDietaRepository extends JpaRepository<DiaEnDieta, Long> {

    List<DiaEnDieta> findByDietaCompleta(DietaCompleta dietaCompleta);

    Optional<DiaEnDieta> findByDiaDeLaSemanaAndDietaCompleta(DiaDeLaSemana diaDeLaSemana,  DietaCompleta dietaCompleta);

}
