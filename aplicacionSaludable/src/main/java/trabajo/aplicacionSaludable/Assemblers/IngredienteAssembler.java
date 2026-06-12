package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.Ingrediente;
import trabajo.aplicacionSaludable.Dtos.IngredienteDTO;

@Component
public class IngredienteAssembler {

    public IngredienteDTO entidadADTO(Ingrediente ingrediente) {
        IngredienteDTO dto = new IngredienteDTO();

        dto.setIdIngrediente(ingrediente.getIdIngrediente());
        dto.setIdAlimento(
                ingrediente.getAlimento() != null
                        ? ingrediente.getAlimento().getIdAlimento()
                        : null
        );

        dto.setIdAlimentoApi(
                ingrediente.getAlimento() != null
                        ? ingrediente.getAlimento().getIdApi()
                        : null
        );

        dto.setNombre(ingrediente.getNombre());
        dto.setCantidadEnGramos(ingrediente.getCantidadEnGramos());
        dto.setCaloriasTotales(ingrediente.getCaloriasTotales());
        dto.setProteinas(ingrediente.getProteinas());
        dto.setCarbohidratos(ingrediente.getCarbohidratos());
        dto.setGrasas(ingrediente.getGrasas());

        return dto;
    }

    public Ingrediente dtoAEntidad(IngredienteDTO dto, Comida comida, Alimento alimento) {

        float factor = dto.getCantidadEnGramos() / 100f;

        Ingrediente ingrediente = new Ingrediente();

        ingrediente.setCantidadEnGramos(dto.getCantidadEnGramos());

        ingrediente.setNombre(alimento.getNombre());

        ingrediente.setCaloriasTotales(alimento.getCalorias() * factor);
        ingrediente.setProteinas(alimento.getProteinas() * factor);
        ingrediente.setCarbohidratos(alimento.getCarbohidratos() * factor);
        ingrediente.setGrasas(alimento.getGrasas() * factor);

        ingrediente.setComida(comida);
        ingrediente.setAlimento(alimento);

        return ingrediente;
    }
}
