package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dtos.ApiEjercicioDTO;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;
import trabajo.aplicacionSaludable.Dtos.RespuestaEjerciciosDTO;
import trabajo.aplicacionSaludable.Repositorios.DiaEnRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.EjercicioRepository;
import trabajo.aplicacionSaludable.Repositorios.RutinaCompletaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EjercicioService {

    private final RestTemplate restTemplate;
    private final EjercicioRepository ejercicioRepository;
    private final DiaEnRutinaRepository diaEnRutinaRepository;
    private final RutinaCompletaRepository rutinaCompletaRepository;

    public EjercicioService(DiaEnRutinaRepository diaEnRutinaRepository, EjercicioRepository ejercicioRepository, RutinaCompletaRepository rutinaCompletaRepository, RestTemplate restTemplate) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.rutinaCompletaRepository = rutinaCompletaRepository;
        this.restTemplate = restTemplate;
    }

    private DiaEnRutina obtenerDia(Long idRutina, DiaDeLaSemana diaSemana) {

        RutinaCompleta rutina = rutinaCompletaRepository.findById(idRutina)
                .orElse(null);

        if (rutina == null) {
            return null;
        }

        return diaEnRutinaRepository
                .findByDiaDeLaSemanaAndRutinaCompleta(diaSemana, rutina)
                .orElse(null);
    }

    public List<ApiEjercicioDTO> obtenerEjercicios() {

        String url = "http://localhost:3000/exercises?page=1&limit=1000";

        RespuestaEjerciciosDTO response =
                restTemplate.getForObject(url, RespuestaEjerciciosDTO.class);

        if (response == null || response.getData() == null) {
            return new ArrayList<>();
        }

        return response.getData();
    }

    private EjercicioDTO EntidadaDTO(Ejercicio ejercicio) {
        EjercicioDTO dto = new EjercicioDTO();
        dto.setIdEjercicio(ejercicio.getIdEjercicio());
        dto.setNombre(ejercicio.getNombre());
        dto.setMusculoEnfocado(ejercicio.getMusculoEnfocado());
        dto.setIdApi(ejercicio.getIdApi());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<EjercicioDTO> listaEjercicios(Long idRutina, DiaDeLaSemana diaDeLaSemana) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        if (dia == null) {
            return null;
        }

        return dia.getEjercicios().stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    @Transactional
    public void anhadirEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, EjercicioDTO ejercicioDTO) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        if (dia == null) {
            return;
        }

        Optional<Ejercicio> ejercicioExiste = ejercicioRepository.findByIdApi(ejercicioDTO.getIdApi());

        Ejercicio ejercicio;
        if (ejercicioExiste.isPresent()) {
            ejercicio = ejercicioExiste.get();
        } else {
            ejercicio = new Ejercicio();
            ejercicio.setNombre(ejercicioDTO.getNombre());
            ejercicio.setIdApi(ejercicioDTO.getIdApi());
            ejercicio.setMusculoEnfocado(ejercicioDTO.getMusculoEnfocado());
            ejercicio = ejercicioRepository.save(ejercicio);
        }
        dia.getEjercicios().add(ejercicio);

    }

    @Transactional
    public boolean borrarEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, Long idEjercicio) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio).orElse(null);

        if (dia == null || ejercicio == null) {
            return false;
        }

        dia.getEjercicios().remove(ejercicio);
        return true;
    }

}
