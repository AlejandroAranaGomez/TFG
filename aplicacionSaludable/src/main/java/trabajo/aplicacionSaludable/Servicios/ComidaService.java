package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.*;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Dtos.ComidaSeguimientoDTO;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
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

    public ComidaService(ComidaRepository comidaRepository, DiaEnDietaRepository diaEnDietaRepository, DietaCompletaRepository dietaCompletaRepository, RegistroComidaDiariaRepository registroComidaDiariaRepository) {
        this.comidaRepository = comidaRepository;
        this.diaEnDietaRepository = diaEnDietaRepository;
        this.dietaCompletaRepository = dietaCompletaRepository;
        this.registroComidaDiariaRepository = registroComidaDiariaRepository;
    }

    private Comida DTOaEntidad(ComidaDTO comidaDTO, DiaEnDieta diaEnDieta) {
        Comida comida = new Comida();
        comida.setNombre(comidaDTO.getNombre());
        comida.setCaloriasTotales(comidaDTO.getCaloriasTotales());
        comida.setProteinas(comidaDTO.getProteinas());
        comida.setCarbohidratos(comidaDTO.getCarbohidratos());
        comida.setGrasas(comidaDTO.getGrasas());
        comida.setDiaEnDieta(diaEnDieta);
        return comida;
    }

    private ComidaDTO EntidadaDTO(Comida comida) {
        ComidaDTO comidaDTO = new ComidaDTO();
        comidaDTO.setIdComida(comida.getIdComida());
        comidaDTO.setNombre(comida.getNombre());
        comidaDTO.setCaloriasTotales(comida.getCaloriasTotales());
        comidaDTO.setProteinas(comida.getProteinas());
        comidaDTO.setCarbohidratos(comida.getCarbohidratos());
        comidaDTO.setGrasas(comida.getGrasas());
        return comidaDTO;
    }

    private ComidaSeguimientoDTO EntidadaSeguimientoDTO(Comida comida, boolean realizada) {
        ComidaSeguimientoDTO comidaDTO = new ComidaSeguimientoDTO();
        comidaDTO.setIdComida(comida.getIdComida());
        comidaDTO.setNombre(comida.getNombre());
        comidaDTO.setCaloriasTotales(comida.getCaloriasTotales());
        comidaDTO.setProteinas(comida.getProteinas());
        comidaDTO.setCarbohidratos(comida.getCarbohidratos());
        comidaDTO.setGrasas(comida.getGrasas());
        comidaDTO.setRegistrada(realizada);

        if (comida.getIngredientes() != null) {
            comidaDTO.setIngredientes(
                    comida.getIngredientes()
                            .stream()
                            .map(this::EntidadIngredienteDTO)
                            .collect(Collectors.toList())
            );
        }

        return comidaDTO;
    }

    private IngredienteDTO EntidadIngredienteDTO(Ingrediente ingrediente) {
        IngredienteDTO ingredienteDTO = new IngredienteDTO();
        ingredienteDTO.setIdIngrediente(ingrediente.getIdIngrediente());
        ingredienteDTO.setNombre(ingrediente.getNombre());
        ingredienteDTO.setCaloriasTotales(ingrediente.getCaloriasTotales());
        ingredienteDTO.setProteinas(ingrediente.getProteinas());
        ingredienteDTO.setCarbohidratos(ingrediente.getCarbohidratos());
        ingredienteDTO.setGrasas(ingrediente.getGrasas());
        return ingredienteDTO;
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

        return comidaRepository.findByDiaEnDieta(diaExiste).stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public ComidaDTO crearComida(Long idDieta, DiaDeLaSemana diaDeLaSemana, ComidaDTO comidaDTO) {
        DiaEnDieta dia = obtenerDia(idDieta, diaDeLaSemana);

        if (dia == null) {
            return null;
        }

        if (comidaRepository.findByNombreAndDiaEnDieta(comidaDTO.getNombre(), dia).isPresent()) {
            throw new ComidaYaExisteException();
        }

        Comida nuevaComida = DTOaEntidad(comidaDTO, dia);
        Comida comidaGuardada = comidaRepository.save(nuevaComida);

        return EntidadaDTO(comidaGuardada);

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
        return EntidadaDTO(comidaActualizada);

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

            return EntidadaSeguimientoDTO(comida, realizada);

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
