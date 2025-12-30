package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaPerteneceAOtroDiaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas.ComidaYaExisteException;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.DiaEnDietaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComidaService {

    private final ComidaRepository comidaRepository;
    private final DiaEnDietaRepository diaEnDietaRepository;

    @Autowired
    public ComidaService(ComidaRepository comidaRepository, DiaEnDietaRepository diaEnDietaRepository) {
        this.comidaRepository = comidaRepository;
        this.diaEnDietaRepository = diaEnDietaRepository;
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

    public List<ComidaDTO> listaComidas(Long idDiaEnDieta) {
        DiaEnDieta diaExiste = diaEnDietaRepository.findById(idDiaEnDieta)
                .orElse(null);

        if (diaExiste == null) {
            return null;
        }

        return comidaRepository.findByDiaEnDieta(diaExiste).stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public ComidaDTO crearComida(Long idDiaEnDieta, ComidaDTO comidaDTO) {
        DiaEnDieta diaExiste = diaEnDietaRepository.findById(idDiaEnDieta)
                .orElse(null);

        if (comidaRepository.findByNombreAndDiaEnDieta(comidaDTO.getNombre(), diaExiste).isPresent()) {
            throw new ComidaYaExisteException();
        }

        Comida nuevaComida = DTOaEntidad(comidaDTO, diaExiste);
        Comida comidaGuardada = comidaRepository.save(nuevaComida);

        return EntidadaDTO(comidaGuardada);

    }

    public ComidaDTO editarComida(Long idDiaEnDieta, Long idComida, ComidaDTO comidaDTO) {
        Comida comida = comidaRepository.findById(idComida)
                .orElse(null);

        if (comida == null) {
            return null;
        }

        if (!comida.getDiaEnDieta().getIdDiaEnDieta().equals(idDiaEnDieta)) {
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

    public boolean borrarComida(Long idDiaEnDieta, Long idComida) {
        Comida comida = comidaRepository.findById(idComida)
                .orElse(null);

        if (comida == null) {
            return false;
        }

        if (!comida.getDiaEnDieta().getIdDiaEnDieta().equals(idDiaEnDieta)) {
            throw new ComidaPerteneceAOtroDiaException();
        }

        comidaRepository.delete(comida);
        recalcularTotales(comida.getDiaEnDieta());

        return true;
    }

}
