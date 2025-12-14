package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.util.List;
import java.util.Optional;

public interface DiaEnDietaRepository extends JpaRepository<DiaEnDieta, Long> {

    List<DiaEnDieta> findByDietaCompleta(DietaCompleta dietaCompleta);

    Optional<DiaEnDieta> findByDiaDeLaSemanaAndDietaCompleta(DiaDeLaSemana diaDeLaSemana,  DietaCompleta dietaCompleta);

}
