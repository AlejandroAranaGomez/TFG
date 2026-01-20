package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;

import java.util.List;
import java.util.Optional;

@Repository
public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    Optional<Ejercicio> findByIdApi(String idApi);
}
