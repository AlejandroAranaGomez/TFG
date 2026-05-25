package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.ComidaAssembler;
import trabajo.aplicacionSaludable.Dominio.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Dtos.ComidaSeguimientoDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaPerteneceAOtroDiaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaYaExisteException;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.DiaEnDietaRepository;
import trabajo.aplicacionSaludable.Repositorios.DietaCompletaRepository;
import trabajo.aplicacionSaludable.Repositorios.RegistroComidaDiariaRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComidaService {

    private final ComidaRepository comidaRepository;
    private final DiaEnDietaRepository diaEnDietaRepository;
    private final DietaCompletaRepository dietaCompletaRepository;
    private final RegistroComidaDiariaRepository registroComidaDiariaRepository;
    private final ComidaAssembler comidaAssembler;

    public ComidaService(ComidaRepository comidaRepository, DiaEnDietaRepository diaEnDietaRepository, DietaCompletaRepository dietaCompletaRepository, RegistroComidaDiariaRepository registroComidaDiariaRepository, ComidaAssembler comidaAssembler) {
        this.comidaRepository = comidaRepository;
        this.diaEnDietaRepository = diaEnDietaRepository;
        this.dietaCompletaRepository = dietaCompletaRepository;
        this.registroComidaDiariaRepository = registroComidaDiariaRepository;
        this.comidaAssembler = comidaAssembler;
    }


    private void recalcularTotales(DiaEnDieta dia) {

        float calDia = 0, pDia = 0, cDia = 0, gDia = 0;

        for (Comida c : dia.getComidas()) {
            calDia += c.getCaloriasTotales();
            pDia += c.getProteinas();
            cDia += c.getCarbohidratos();
            gDia += c.getGrasas();
        }

        dia.setCaloriasTotales(calDia);
        dia.setProteinas(pDia);
        dia.setCarbohidratos(cDia);
        dia.setGrasas(gDia);

        DietaCompleta dieta = dia.getDietaCompleta();
        if (dieta != null) {
            float calDieta = 0, pDieta = 0, cDieta = 0, gDieta = 0;
            for (DiaEnDieta d : dieta.getDiasDeDieta()) {
                calDieta += d.getCaloriasTotales();
                pDieta += d.getProteinas();
                cDieta += d.getCarbohidratos();
                gDieta += d.getGrasas();
            }
            dieta.setCaloriasTotales(calDieta);
            dieta.setProteinas(pDieta);
            dieta.setCarbohidratos(cDieta);
            dieta.setGrasas(gDieta);
        }

        diaEnDietaRepository.save(dia);
    }

    private DiaEnDieta obtenerDia(Long idDieta, DiaDeLaSemana diaSemana) {

        DietaCompleta dieta = dietaCompletaRepository.findById(idDieta)
                .orElse(null);

        if (dieta == null) {
            return null;
        }

        return diaEnDietaRepository
                .findByDiaDeLaSemanaAndDietaCompleta(diaSemana, dieta)
                .orElse(null);
    }

    public List<ComidaDTO> listaComidas(Long idDieta, DiaDeLaSemana diaDeLaSemana) {

        DiaEnDieta diaExiste = obtenerDia(idDieta, diaDeLaSemana);

        if (diaExiste == null) {
            return null;
        }

        return comidaRepository.findByDiaEnDieta(diaExiste).stream().map(comidaAssembler::entidadADTO).collect(Collectors.toList());
    }

    public ComidaDTO crearComida(Long idDieta, DiaDeLaSemana diaDeLaSemana, ComidaDTO comidaDTO) {
        DiaEnDieta dia = obtenerDia(idDieta, diaDeLaSemana);

        if (dia == null) {
            return null;
        }

        if (comidaRepository.findByNombreAndDiaEnDieta(comidaDTO.getNombre(), dia).isPresent()) {
            throw new ComidaYaExisteException();
        }

        Comida nuevaComida = comidaAssembler.dtoAEntidad(comidaDTO, dia);
        Comida comidaGuardada = comidaRepository.save(nuevaComida);

        return comidaAssembler.entidadADTO(comidaGuardada);

    }

    public ComidaDTO editarComida(Long idDieta, DiaDeLaSemana diaDeLaSemana, Long idComida, ComidaDTO comidaDTO) {
        DiaEnDieta dia = obtenerDia(idDieta, diaDeLaSemana);

        if (dia == null) {
            return null;
        }
        Comida comida = comidaRepository.findById(idComida)
                .orElse(null);

        if (comida == null) {
            return null;
        }

        if (!comida.getDiaEnDieta().getIdDiaEnDieta().equals(dia.getIdDiaEnDieta())) {
            throw new ComidaPerteneceAOtroDiaException();
        }

        Optional<Comida> otraComidaMismoNombre = comidaRepository.findByNombreAndDiaEnDieta(comidaDTO.getNombre(), comida.getDiaEnDieta());
        if (otraComidaMismoNombre.isPresent() && !otraComidaMismoNombre.get().getIdComida().equals(idComida)) {
            throw new ComidaYaExisteException();
        }

        comida.setNombre(comidaDTO.getNombre());

        Comida comidaActualizada = comidaRepository.save(comida);
        return comidaAssembler.entidadADTO(comidaActualizada);

    }

    public boolean borrarComida(Long idDieta, DiaDeLaSemana diaDeLaSemana, Long idComida) {
        DiaEnDieta dia = obtenerDia(idDieta, diaDeLaSemana);

        if (dia == null) {
            return false;
        }

        Comida comida = comidaRepository.findById(idComida)
                .orElse(null);

        if (comida == null) {
            return false;
        }

        if (!comida.getDiaEnDieta().getIdDiaEnDieta().equals(dia.getIdDiaEnDieta())) {
            throw new ComidaPerteneceAOtroDiaException();
        }

        comidaRepository.delete(comida);
        recalcularTotales(comida.getDiaEnDieta());

        return true;
    }

    public List<ComidaSeguimientoDTO> obtenerComidasHoy(Long idUsuario) {
        DietaCompleta dietaActiva = dietaCompletaRepository.findByUsuarioIdUsuarioAndActivaTrue(idUsuario);

        if (dietaActiva == null) {
            return List.of();
        }

        DayOfWeek hoy = LocalDate.now().getDayOfWeek();
        DiaDeLaSemana dia = convertirDia(hoy);

        DiaEnDieta diaEnDieta = obtenerDia(dietaActiva.getIdDietaCompleta(), dia);

        List<Comida> comidas = comidaRepository.findByDiaEnDieta(diaEnDieta);


        List<RegistroComidaDiaria> registrosHoy = registroComidaDiariaRepository.findByUsuarioIdUsuarioAndFecha(idUsuario, LocalDate.now());

        return comidas.stream().map(comida -> {

            boolean realizada = registrosHoy.stream()
                    .anyMatch(r -> r.getComida().getIdComida().equals(comida.getIdComida()));

            return comidaAssembler.entidadASeguimientoDTO(comida, realizada);

        }).collect(Collectors.toList());
    }

    private DiaDeLaSemana convertirDia(DayOfWeek hoy) {

        switch (hoy) {
            case MONDAY: return DiaDeLaSemana.LUNES;
            case TUESDAY: return DiaDeLaSemana.MARTES;
            case WEDNESDAY: return DiaDeLaSemana.MIERCOLES;
            case THURSDAY: return DiaDeLaSemana.JUEVES;
            case FRIDAY: return DiaDeLaSemana.VIERNES;
            case SATURDAY: return DiaDeLaSemana.SABADO;
            case SUNDAY: return DiaDeLaSemana.DOMINGO;
            default: throw new IllegalArgumentException("Día inválido");
        }
    }

}
