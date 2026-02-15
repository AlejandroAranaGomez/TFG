package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Credenciales;

import java.util.Optional;

@Repository
public interface CredencialesRepository extends JpaRepository<Credenciales, Long> {
    Optional<Credenciales> findByEmail(String email);
}
