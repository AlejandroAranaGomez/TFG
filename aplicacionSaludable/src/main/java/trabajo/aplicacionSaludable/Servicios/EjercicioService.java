package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import trabajo.aplicacionSaludable.Assemblers.EjercicioAssembler;
import trabajo.aplicacionSaludable.Dominio.*;
import trabajo.aplicacionSaludable.Dtos.ApiEjercicioDTO;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;
import trabajo.aplicacionSaludable.Dtos.RespuestaEjerciciosDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesEjercicios.EjercicioDuplicadoException;
import trabajo.aplicacionSaludable.Repositorios.DiaEnRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.EjercicioEnDiaRutinaRepository;
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
    private final EjercicioEnDiaRutinaRepository ejercicioEnDiaRutinaRepository;
    private final EjercicioAssembler ejercicioAssembler;

    public EjercicioService(DiaEnRutinaRepository diaEnRutinaRepository, EjercicioRepository ejercicioRepository, RutinaCompletaRepository rutinaCompletaRepository, EjercicioEnDiaRutinaRepository ejercicioEnDiaRutinaRepository, EjercicioAssembler ejercicioAssembler,  RestTemplate restTemplate) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.rutinaCompletaRepository = rutinaCompletaRepository;
        this.ejercicioEnDiaRutinaRepository = ejercicioEnDiaRutinaRepository;
        this.ejercicioAssembler = ejercicioAssembler;
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

    @Transactional(readOnly = true)
    public List<EjercicioDTO> listaEjercicios(Long idRutina, DiaDeLaSemana diaDeLaSemana) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        if (dia == null) {
            return null;
        }

        return dia.getEjerciciosAsignados().stream()
                .map(ejercicioAssembler::entidadADTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void anhadirEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, EjercicioDTO ejercicioDTO) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        if (dia == null) {
            return;
        }

        System.out.println("ID API RECIBIDO: " + ejercicioDTO.getIdApi());

        Optional<Ejercicio> ejercicioExiste = ejercicioRepository.findByIdApi(ejercicioDTO.getIdApi());

        if (ejercicioExiste.isPresent()) {
            System.out.println("EJERCICIO ENCONTRADO: " + ejercicioExiste.get().getIdEjercicio()
                    + " - " + ejercicioExiste.get().getNombre());
        }

        Ejercicio ejercicio;
        if (ejercicioExiste.isPresent()) {
            ejercicio = ejercicioExiste.get();
        } else {
            ejercicio = ejercicioAssembler.dtoAEntidad(ejercicioDTO);
            ejercicio = ejercicioRepository.save(ejercicio);
        }

        if (ejercicioEnDiaRutinaRepository.findByDiaEnRutinaAndEjercicio(dia, ejercicio).isPresent()) {
            throw new EjercicioDuplicadoException();
        }

        EjercicioEnDiaRutina ejercicioAsignado = new EjercicioEnDiaRutina();
        ejercicioAsignado.setDiaEnRutina(dia);
        ejercicioAsignado.setEjercicio(ejercicio);

        ejercicioEnDiaRutinaRepository.save(ejercicioAsignado);
    }

    @Transactional
    public boolean borrarEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, Long idEjercicio) {
        DiaEnRutina dia = obtenerDia(idRutina, diaDeLaSemana);

        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio).orElse(null);

        if (dia == null || ejercicio == null) {
            return false;
        }

        EjercicioEnDiaRutina ejercicioAsignado = ejercicioEnDiaRutinaRepository.findByDiaEnRutinaAndEjercicio(dia, ejercicio).orElse(null);

        if (ejercicioAsignado == null) {
            return false;
        }

        ejercicioEnDiaRutinaRepository.delete(ejercicioAsignado);

        return true;
    }

}
