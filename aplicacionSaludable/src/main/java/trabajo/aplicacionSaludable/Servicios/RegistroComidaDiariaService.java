package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.RegistroComidaDiaria;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.RegistroComidaDiariaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento.ComidaNoRegistradaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento.ComidaYaRegistradaException;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.RegistroComidaDiariaRepository;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class RegistroComidaDiariaService {

    private final RegistroComidaDiariaRepository registroComidaDiariaRepository;
    private final ComidaRepository comidaRepository;
    private final UsuarioRepository usuarioRepository;

    public RegistroComidaDiariaService(RegistroComidaDiariaRepository registroComidaDiariaRepository, ComidaRepository comidaRepository, UsuarioRepository usuarioRepository) {
        this.registroComidaDiariaRepository = registroComidaDiariaRepository;
        this.comidaRepository = comidaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private RegistroComidaDiariaDTO EntidadADTORegistro(RegistroComidaDiaria registro) {
        RegistroComidaDiariaDTO dto = new RegistroComidaDiariaDTO();

        dto.setIdRegistroComidaDiaria(registro.getIdRegistroComidaDiaria());
        dto.setFecha(registro.getFecha().toString());

        dto.setCaloriasTotales(registro.getComida().getCaloriasTotales());
        dto.setProteinas(registro.getComida().getProteinas());
        dto.setCarbohidratos(registro.getComida().getCarbohidratos());
        dto.setGrasas(registro.getComida().getGrasas());

        return dto;
    }

    public RegistroComidaDiariaDTO registrarComidaHoy(Long idUsuario, Long idComida) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return null;
        }

        Comida comida = comidaRepository.findById(idComida).orElse(null);

        if (comida == null) {
            return null;
        }

        LocalDate hoy = LocalDate.now();

        boolean yaExiste = registroComidaDiariaRepository.existsByUsuarioAndComidaAndFecha(usuario, comida, hoy);

        if (yaExiste) {
            throw new ComidaYaRegistradaException();
        }

        RegistroComidaDiaria registro = new RegistroComidaDiaria();
        registro.setUsuario(usuario);
        registro.setComida(comida);
        registro.setFecha(hoy);
        registro.setCaloriasTotales(comida.getCaloriasTotales());
        registro.setProteinas(comida.getProteinas());
        registro.setCarbohidratos(comida.getCarbohidratos());
        registro.setGrasas(comida.getGrasas());

        RegistroComidaDiaria registroGuardado = registroComidaDiariaRepository.save(registro);

        return EntidadADTORegistro(registroGuardado);
    }

    public boolean eliminarComidaHoy(Long idUsuario, Long idComida) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario == null) {
            return false;
        }

        Comida comida = comidaRepository.findById(idComida).orElse(null);

        if (comida == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();

        Optional<RegistroComidaDiaria> registroComidaDiaria = registroComidaDiariaRepository.findByUsuarioAndComidaAndFecha(usuario, comida, hoy);

        if (registroComidaDiaria.isEmpty()) {
            throw new ComidaNoRegistradaException();
        }

        registroComidaDiariaRepository.delete(registroComidaDiaria.get());

        return true;
    }
}
