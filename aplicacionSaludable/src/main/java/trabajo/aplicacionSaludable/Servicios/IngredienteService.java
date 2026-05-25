package trabajo.aplicacionSaludable.Servicios;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.IngredienteAssembler;
import trabajo.aplicacionSaludable.Dominio.*;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.CantidadNegativaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteDuplicadoException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes.IngredienteNoPerteneceADietaException;
import trabajo.aplicacionSaludable.Repositorios.AlimentoRepository;
import trabajo.aplicacionSaludable.Repositorios.ComidaRepository;
import trabajo.aplicacionSaludable.Repositorios.IngredienteRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IngredienteService {

    private final ComidaRepository comidaRepository;
    private final IngredienteRepository ingredienteRepository;
    private final AlimentoRepository alimentoRepository;
    private final IngredienteAssembler ingredienteAssembler;

    public IngredienteService(ComidaRepository comidaRepository, IngredienteRepository ingredienteRepository, AlimentoRepository alimentoRepository, IngredienteAssembler ingredienteAssembler) {
        this.comidaRepository = comidaRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.alimentoRepository = alimentoRepository;
        this.ingredienteAssembler = ingredienteAssembler;
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

        return comida.getIngredientes().stream().map(ingredienteAssembler::entidadADTO).collect(Collectors.toList());
    }

    public IngredienteDTO crearIngrediente(Long idComida, IngredienteDTO ingredienteDTO) {
        Comida comida = comidaRepository.findById(idComida).orElse(null);

        if (comida == null) {
            return  null;
        }

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            throw new CantidadNegativaException();
        }

        Alimento alimento = null;

        if (ingredienteDTO.getIdAlimento() != null) {

            alimento = alimentoRepository.findById(ingredienteDTO.getIdAlimento()).orElse(null);

        } else if (ingredienteDTO.getIdAlimentoApi() != null) {

            alimento = alimentoRepository.findByIdApi(ingredienteDTO.getIdAlimentoApi()).orElse(null);

            System.out.println("ID API recibido: " + ingredienteDTO.getIdAlimentoApi());

            if (alimento != null) {
                System.out.println("ALIMENTO ENCONTRADO -> ID: " + alimento.getIdAlimento() + " NOMBRE: " + alimento.getNombre());
            }

            // Si no existe le creo
            if (alimento == null) {

                alimento = new Alimento();

                alimento.setNombre(ingredienteDTO.getNombre());
                alimento.setCalorias(ingredienteDTO.getCaloriasTotales());
                alimento.setProteinas(ingredienteDTO.getProteinas());
                alimento.setCarbohidratos(ingredienteDTO.getCarbohidratos());
                alimento.setGrasas(ingredienteDTO.getGrasas());

                alimento.setIdApi(ingredienteDTO.getIdAlimentoApi());

                alimento = alimentoRepository.save(alimento);
            }
        } else {
            return null;
        }


        if (ingredienteRepository.findByAlimentoAndComida(alimento, comida).isPresent()) {
            throw new IngredienteDuplicadoException();
        }

        Ingrediente ingrediente = ingredienteAssembler.dtoAEntidad(ingredienteDTO, comida, alimento);

        Ingrediente ingredienteGuardado = ingredienteRepository.save(ingrediente);

        recalcularTotales(comida);

        return ingredienteAssembler.entidadADTO(ingredienteGuardado);

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
        return ingredienteAssembler.entidadADTO(ingredienteActualizado);
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
