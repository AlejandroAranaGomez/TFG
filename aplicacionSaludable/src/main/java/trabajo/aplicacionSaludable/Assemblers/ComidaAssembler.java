package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.Ingrediente;
import trabajo.aplicacionSaludable.Dtos.ComidaDTO;
import trabajo.aplicacionSaludable.Dtos.ComidaSeguimientoDTO;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;

import java.util.stream.Collectors;

@Component
public class ComidaAssembler {

    public Comida dtoAEntidad(ComidaDTO comidaDTO, DiaEnDieta diaEnDieta) {
        Comida comida = new Comida();
        comida.setNombre(comidaDTO.getNombre());
        comida.setCaloriasTotales(comidaDTO.getCaloriasTotales());
        comida.setProteinas(comidaDTO.getProteinas());
        comida.setCarbohidratos(comidaDTO.getCarbohidratos());
        comida.setGrasas(comidaDTO.getGrasas());
        comida.setDiaEnDieta(diaEnDieta);
        return comida;
    }

    public ComidaDTO entidadADTO(Comida comida) {
        ComidaDTO comidaDTO = new ComidaDTO();
        comidaDTO.setIdComida(comida.getIdComida());
        comidaDTO.setNombre(comida.getNombre());
        comidaDTO.setCaloriasTotales(comida.getCaloriasTotales());
        comidaDTO.setProteinas(comida.getProteinas());
        comidaDTO.setCarbohidratos(comida.getCarbohidratos());
        comidaDTO.setGrasas(comida.getGrasas());
        return comidaDTO;
    }

    public ComidaSeguimientoDTO entidadASeguimientoDTO(Comida comida, boolean realizada) {
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
                            .map(this::ingredienteADTO)
                            .collect(Collectors.toList())
            );
        }

        return comidaDTO;
    }

    public IngredienteDTO ingredienteADTO(Ingrediente ingrediente) {
        IngredienteDTO ingredienteDTO = new IngredienteDTO();

        ingredienteDTO.setIdIngrediente(ingrediente.getIdIngrediente());
        ingredienteDTO.setNombre(ingrediente.getAlimento().getNombre());
        ingredienteDTO.setCaloriasTotales(ingrediente.getCaloriasTotales());
        ingredienteDTO.setProteinas(ingrediente.getProteinas());
        ingredienteDTO.setCarbohidratos(ingrediente.getCarbohidratos());
        ingredienteDTO.setGrasas(ingrediente.getGrasas());

        return ingredienteDTO;
    }
}
