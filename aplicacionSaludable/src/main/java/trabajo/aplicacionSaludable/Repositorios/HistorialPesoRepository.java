package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.HistorialPeso;

import java.util.List;

@Repository
public interface HistorialPesoRepository extends JpaRepository<HistorialPeso, Long> {

    List<HistorialPeso> findByUsuarioIdUsuarioOrderByFechaAsc(Long idUsuario);

}
