package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.RegistroComidaDiaria;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroComidaDiariaRepository extends JpaRepository<RegistroComidaDiaria, Long> {

    boolean existsByUsuarioAndComidaAndFecha(Usuario usuario, Comida comida, LocalDate fecha);

    List<RegistroComidaDiaria> findByUsuarioIdUsuarioAndFecha(Long idUsuario, LocalDate fecha);

    Optional<RegistroComidaDiaria> findByUsuarioAndComidaAndFecha(Usuario usuario, Comida comida, LocalDate fecha);
}
