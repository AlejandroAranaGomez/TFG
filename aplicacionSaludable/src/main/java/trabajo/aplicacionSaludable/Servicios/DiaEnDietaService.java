package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta.DiaPerteneceAOtraDietaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta.DiaYaCreadoException;
import trabajo.aplicacionSaludable.Repositorios.DiaEnDietaRepository;
import trabajo.aplicacionSaludable.Repositorios.DietaCompletaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaEnDietaService {

    private final DietaCompletaRepository dietaCompletaRepository;
    private final DiaEnDietaRepository diaEnDietaRepository;

    @Autowired
    public DiaEnDietaService(DiaEnDietaRepository diaEnDietaRepository, DietaCompletaRepository dietaCompletaRepository) {
        this.diaEnDietaRepository = diaEnDietaRepository;
        this.dietaCompletaRepository = dietaCompletaRepository;
    }

    private DiaEnDieta DTOaEntidad(DiaEnDietaDTO diaEnDietaDTO, DietaCompleta dietaCompleta) {
        DiaEnDieta diaEnDieta = new DiaEnDieta();
        diaEnDieta.setNombre(diaEnDietaDTO.getNombre());
        diaEnDieta.setCaloriasTotales(diaEnDietaDTO.getCaloriasTotales());
        diaEnDieta.setProteinas(diaEnDietaDTO.getProteinas());
        diaEnDieta.setCarbohidratos(diaEnDietaDTO.getCarbohidratos());
        diaEnDieta.setGrasas(diaEnDietaDTO.getGrasas());
        diaEnDieta.setDiaDeLaSemana(diaEnDietaDTO.getDiaDeLaSemana());
        diaEnDieta.setDietaCompleta(dietaCompleta);
        return diaEnDieta;
    }

    private DiaEnDietaDTO EntidadaDTO(DiaEnDieta diaEnDieta) {
        DiaEnDietaDTO diaEnDietaDTO = new DiaEnDietaDTO();
        diaEnDietaDTO.setIdDiaEnDieta(diaEnDieta.getIdDiaEnDieta());
        diaEnDietaDTO.setNombre(diaEnDieta.getNombre());
        diaEnDietaDTO.setCaloriasTotales(diaEnDieta.getCaloriasTotales());
        diaEnDietaDTO.setProteinas(diaEnDieta.getProteinas());
        diaEnDietaDTO.setCarbohidratos(diaEnDieta.getCarbohidratos());
        diaEnDietaDTO.setGrasas(diaEnDieta.getGrasas());
        diaEnDietaDTO.setDiaDeLaSemana(diaEnDieta.getDiaDeLaSemana());
        return diaEnDietaDTO;
    }

    private void recalcularTotales(DietaCompleta dieta) {
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

        dietaCompletaRepository.save(dieta);
    }


    public List<DiaEnDietaDTO> listaDiaEnDieta(Long idDietaCompleta) {
        DietaCompleta dietaCompleta = dietaCompletaRepository.findByIdDietaCompleta(idDietaCompleta)
                .orElse(null);

        if (dietaCompleta == null) {
            return null;
        }

        List<DiaEnDieta> diasSinOrdenar = diaEnDietaRepository.findByDietaCompleta(dietaCompleta);

        List<DiaEnDieta> diasOrdenados = diasSinOrdenar.stream()
                .sorted(Comparator.comparing(DiaEnDieta::getDiaDeLaSemana))
                .collect(Collectors.toList());

        return diasOrdenados.stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public DiaEnDietaDTO crearDiaEnDieta(DiaEnDietaDTO diaEnDietaDTO, Long idDietaCompleta) {

        DietaCompleta dietaCompleta = dietaCompletaRepository.findByIdDietaCompleta(idDietaCompleta)
                .orElse(null);

        if (dietaCompleta == null) {
            return null;
        }

        if (diaEnDietaRepository.findByDiaDeLaSemanaAndDietaCompleta(diaEnDietaDTO.getDiaDeLaSemana(), dietaCompleta).isPresent()) {
            throw new DiaYaCreadoException();
        }

        DiaEnDieta nuevoDiaEnDieta = DTOaEntidad(diaEnDietaDTO, dietaCompleta);
        DiaEnDieta diaGuardado = diaEnDietaRepository.save(nuevoDiaEnDieta);

        return EntidadaDTO(diaGuardado);

    }

    public DiaEnDietaDTO editarDiaEnDieta(DiaEnDietaDTO diaEnDietaDTO, Long idDietaCompleta, Long idDiaEnDieta) {
        DiaEnDieta diaExiste = diaEnDietaRepository.findById(idDiaEnDieta)
                .orElse(null);

        if  (diaExiste == null) {
            return null;
        }

        if (!diaExiste.getDietaCompleta().getIdDietaCompleta().equals(idDietaCompleta)) {
            throw new DiaPerteneceAOtraDietaException();
        }

        diaExiste.setNombre(diaEnDietaDTO.getNombre());

        DiaEnDieta actualizado = diaEnDietaRepository.save(diaExiste);
        return EntidadaDTO(actualizado);
    }

    public boolean borrarDia(Long idDiaEnDieta, Long idDietaCompleta) {
        DiaEnDieta diaEnDieta = diaEnDietaRepository.findById(idDiaEnDieta).orElse(null);

        if (diaEnDieta == null) {
            return false;
        }

        if (!diaEnDieta.getDietaCompleta().getIdDietaCompleta().equals(idDietaCompleta)) {
            throw new DiaPerteneceAOtraDietaException();
        }

        diaEnDietaRepository.deleteById(idDiaEnDieta);

        recalcularTotales(diaEnDieta.getDietaCompleta());
        return true;
    }
}
