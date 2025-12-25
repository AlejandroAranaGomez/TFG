package trabajo.aplicacionSaludable.Servicios;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Ingrediente;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.IngredienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredienteService {

    private final ComidaRepository comidaRepository;
    private final IngredienteRepository ingredienteRepository;

    @Autowired
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

    public List<IngredienteDTO> listaIngredientes(Long idComida) throws Exception {
        Comida comida = comidaRepository.findById(idComida).orElseThrow(() -> new Exception("Comida no encontrada con este id"));

        return comida.getIngredientes().stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public IngredienteDTO crearIngrediente(Long idComida, IngredienteDTO ingredienteDTO) throws Exception {
        Comida comida = comidaRepository.findById(idComida).orElseThrow(() -> new Exception("Comida no encontrada con este id"));

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            throw new Exception("La cantidad en gramos debe ser mayor que 0");
        }

        Ingrediente nuevoIngrediente = DTOaEntidad(ingredienteDTO, comida);
        Ingrediente ingredienteGuardado = ingredienteRepository.save(nuevoIngrediente);
        recalcularTotales(comida);
        return EntidadaDTO(ingredienteGuardado);

    }

    public IngredienteDTO editarIngrediente(Long idComida, Long idIngrediente, IngredienteDTO ingredienteDTO) throws Exception {
        Comida comida = comidaRepository.findById(idComida).orElseThrow(() -> new Exception("Comida no encontrada con este id"));

        Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente).orElseThrow(() -> new Exception("Ingrediente no encontrado con este id"));

        if (!ingrediente.getComida().getIdComida().equals(idComida)) {
            throw new Exception("Este ingrediente no pertenece a esta comida");
        }

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            throw new Exception("La cantidad en gramos debe ser mayor que 0");
        }

        float factor = ingredienteDTO.getCantidadEnGramos() / ingrediente.getCantidadEnGramos();

        ingrediente.setCantidadEnGramos(ingredienteDTO.getCantidadEnGramos());
        ingrediente.setCaloriasTotales(ingrediente.getCaloriasTotales() * factor);
        ingrediente.setProteinas(ingrediente.getProteinas() * factor);
        ingrediente.setCarbohidratos(ingrediente.getCarbohidratos() * factor);
        ingrediente.setGrasas(ingrediente.getGrasas() * factor);

        Ingrediente ingredienteActualizado = ingredienteRepository.save(ingrediente);
        recalcularTotales(comida);
        return EntidadaDTO(ingredienteActualizado);
    }

    public  void borrarIngrediente(Long idIngrediente, Long idComida) throws Exception {

        Comida comida = comidaRepository.findById(idComida).orElseThrow(() -> new Exception("Comida no encontrada con este id"));

        Ingrediente ingrediente = ingredienteRepository.findById(idIngrediente).orElseThrow(() -> new Exception("Ingrediente no encontrado con este id"));

        if (!ingrediente.getComida().getIdComida().equals(idComida)) {
            throw new Exception("Este ingrediente no pertenece a esta comida");
        }

        // Necesario para calcular las propiedades y que el alimentos se borre correctamente.
        comida.getIngredientes().remove(ingrediente);

        ingredienteRepository.delete(ingrediente);
        ingredienteRepository.flush();

        recalcularTotales(comida);
    }


}
