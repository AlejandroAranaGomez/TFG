package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.Ingrediente;

import java.util.Optional;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    Optional<Ingrediente> findByAlimentoAndComida(Alimento alimento, Comida comida);
}
