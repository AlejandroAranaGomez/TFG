package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;
import trabajo.aplicacionSaludable.Dominio.EjercicioEnDiaRutina;

import java.util.Optional;

@Repository
public interface EjercicioEnDiaRutinaRepository extends JpaRepository<EjercicioEnDiaRutina, Long>  {

    Optional<EjercicioEnDiaRutina> findByDiaEnRutinaAndEjercicio(DiaEnRutina diaEnRutina, Ejercicio ejercicio);

}
