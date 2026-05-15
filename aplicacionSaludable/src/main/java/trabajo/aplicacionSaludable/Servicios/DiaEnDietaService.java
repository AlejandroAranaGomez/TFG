package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.DiaEnDietaAssembler;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Repositorios.DiaEnDietaRepository;
import trabajo.aplicacionSaludable.Repositorios.DietaCompletaRepository;

import java.util.Optional;

@Service
public class DiaEnDietaService {

    private final DietaCompletaRepository dietaCompletaRepository;
    private final DiaEnDietaRepository diaEnDietaRepository;
    private final DiaEnDietaAssembler diaEnDietaAssembler;

    public DiaEnDietaService(DiaEnDietaRepository diaEnDietaRepository, DietaCompletaRepository dietaCompletaRepository, DiaEnDietaAssembler diaEnDietaAssembler) {
        this.diaEnDietaRepository = diaEnDietaRepository;
        this.dietaCompletaRepository = dietaCompletaRepository;
        this.diaEnDietaAssembler = diaEnDietaAssembler;
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


    public DiaEnDietaDTO guardarDiaEnDieta(DiaEnDietaDTO diaEnDietaDTO, Long idDietaCompleta, DiaDeLaSemana diaDeLaSemana) {
        DietaCompleta dieta = dietaCompletaRepository.findById(idDietaCompleta)
                .orElse(null);

        if (dieta == null) {
            return null;
        }

        Optional<DiaEnDieta> diaEnDieta = diaEnDietaRepository.findByDiaDeLaSemanaAndDietaCompleta(diaDeLaSemana, dieta);

        DiaEnDieta dia;

        if (diaEnDieta.isPresent()) {
            dia = diaEnDieta.get();
        } else {
            dia = new DiaEnDieta();
            dia.setDietaCompleta(dieta);
            dia.setDiaDeLaSemana(diaDeLaSemana);
        }

        dia.setNombre(diaEnDietaDTO.getNombre());
        dia.setCaloriasTotales(diaEnDietaDTO.getCaloriasTotales());
        dia.setProteinas(diaEnDietaDTO.getProteinas());
        dia.setCarbohidratos(diaEnDietaDTO.getCarbohidratos());
        dia.setGrasas(diaEnDietaDTO.getGrasas());

        DiaEnDieta guardado = diaEnDietaRepository.save(dia);

        recalcularTotales(dieta);

        return diaEnDietaAssembler.entidadADTO(guardado);
    }

    public boolean borrarDia(DiaDeLaSemana diaDeLaSemana, Long idDietaCompleta) {
        DietaCompleta dieta = dietaCompletaRepository.findById(idDietaCompleta)
                .orElse(null);

        if (dieta == null) {
            return false;
        }

        Optional<DiaEnDieta> diaOpt = diaEnDietaRepository.findByDiaDeLaSemanaAndDietaCompleta(diaDeLaSemana, dieta);

        if (diaOpt.isEmpty()) {
            return false;
        }

        DiaEnDieta dia = diaOpt.get();
        diaEnDietaRepository.delete(dia);
        recalcularTotales(dieta);

        return true;
    }
}
