package trabajo.aplicacionSaludable.Servicios;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Ingrediente;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.CantidadNegativaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteNoPerteneceADietaException;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.IngredienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredienteService {

    private final ComidaRepository comidaRepository;
    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(ComidaRepository comidaRepository, IngredienteRepository ingredienteRepository) {
        this.comidaRepository = comidaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    private Ingrediente DTOaEntidad(IngredienteDTO dto, Comida comida) {
        return new Ingrediente(
                dto.getCantidadEnGramos(),
                dto.getNombre(),
                dto.getCaloriasTotales(),
                dto.getProteinas(),
                dto.getCarbohidratos(),
                dto.getGrasas(),
                comida
        );
    }

    private IngredienteDTO EntidadaDTO(Ingrediente ingrediente) {
        IngredienteDTO ingredienteDTO = new IngredienteDTO();
        ingredienteDTO.setIdIngrediente(ingrediente.getIdIngrediente());
        ingredienteDTO.setCantidadEnGramos(ingrediente.getCantidadEnGramos());
        ingredienteDTO.setNombre(ingrediente.getNombre());
        ingredienteDTO.setCaloriasTotales(ingrediente.getCaloriasTotales());
        ingredienteDTO.setProteinas(ingrediente.getProteinas());
        ingredienteDTO.setCarbohidratos(ingrediente.getCarbohidratos());
        ingredienteDTO.setGrasas(ingrediente.getGrasas());
        return ingredienteDTO;
    }

    private void recalcularTotales(Comida comida) {

        float cal = 0, p = 0, c = 0, g = 0;

        for (Ingrediente i : comida.getIngredientes()) {
            cal += i.getCaloriasTotales();
            p += i.getProteinas();
            c += i.getCarbohidratos();
            g += i.getGrasas();
        }

        comida.setCaloriasTotales(cal);
        comida.setProteinas(p);
        comida.setCarbohidratos(c);
        comida.setGrasas(g);

        DiaEnDieta dia = comida.getDiaEnDieta();

        float calDia = 0, pDia = 0, cDia = 0, gDia = 0;

        for (Comida cda : dia.getComidas()) {
            calDia += cda.getCaloriasTotales();
            pDia += cda.getProteinas();
            cDia += cda.getCarbohidratos();
            gDia += cda.getGrasas();
        }

        dia.setCaloriasTotales(calDia);
        dia.setProteinas(pDia);
        dia.setCarbohidratos(cDia);
        dia.setGrasas(gDia);

        DietaCompleta dieta = dia.getDietaCompleta();

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

        comidaRepository.save(comida);
    }

    public List<IngredienteDTO> listaIngredientes(Long idComida) {
        Comida comida = comidaRepository.findById(idComida).orElse(null);

        if (comida == null) {
            return null;
        }

        return comida.getIngredientes().stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public IngredienteDTO crearIngrediente(Long idComida, IngredienteDTO ingredienteDTO) {
        Comida comida = comidaRepository.findById(idComida).orElse(null);

        if (comida == null) {
            return  null;
        }

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            throw new CantidadNegativaException();
        }

        if (ingredienteRepository.findByNombreAndComida(ingredienteDTO.getNombre(), comida).isPresent()) {
            throw new IngredienteDuplicadoException();
        }

        Ingrediente nuevoIngrediente = DTOaEntidad(ingredienteDTO, comida);
        Ingrediente ingredienteGuardado = ingredienteRepository.save(nuevoIngrediente);
        recalcularTotales(comida);
        return EntidadaDTO(ingredienteGuardado);

    }

    public IngredienteDTO editarIngrediente(Long idComida, Long idIngrediente, IngredienteDTO ingredienteDTO) {

        Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente).orElse(null);

        if (!ingrediente.getComida().getIdComida().equals(idComida)) {
            throw new IngredienteNoPerteneceADietaException();
        }

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            throw new CantidadNegativaException();
        }

        float factor = ingredienteDTO.getCantidadEnGramos() / ingrediente.getCantidadEnGramos();

        ingrediente.setCantidadEnGramos(ingredienteDTO.getCantidadEnGramos());
        ingrediente.setCaloriasTotales(ingrediente.getCaloriasTotales() * factor);
        ingrediente.setProteinas(ingrediente.getProteinas() * factor);
        ingrediente.setCarbohidratos(ingrediente.getCarbohidratos() * factor);
        ingrediente.setGrasas(ingrediente.getGrasas() * factor);

        Ingrediente ingredienteActualizado = ingredienteRepository.save(ingrediente);
        recalcularTotales(ingrediente.getComida());
        return EntidadaDTO(ingredienteActualizado);
    }

    public boolean borrarIngrediente(Long idIngrediente, Long idComida) {

        Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente).orElse(null);

        if  (ingrediente == null) {
            return false;
        }

        if (!ingrediente.getComida().getIdComida().equals(idComida)) {
            throw new IngredienteNoPerteneceADietaException();
        }

        // Necesario para calcular las propiedades y que el alimentos se borre correctamente.
        ingrediente.getComida().getIngredientes().remove(ingrediente);

        ingredienteRepository.delete(ingrediente);
        ingredienteRepository.flush();

        recalcularTotales(ingrediente.getComida());

        return true;
    }


}
