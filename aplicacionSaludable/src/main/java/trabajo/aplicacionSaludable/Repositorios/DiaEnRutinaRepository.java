package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.*;
import java.util.Optional;

@Repository
public interface DiaEnRutinaRepository extends JpaRepository<DiaEnRutina, Long> {

    Optional<DiaEnRutina> findByDiaDeLaSemanaAndRutinaCompleta(DiaDeLaSemana diaDeLaSemana, RutinaCompleta rutinaCompleta);
}
