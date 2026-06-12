package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.AlimentoDTO;

// Estas clases sirven para transformar los datos del dominio a DTOs y viceversa.

@Component
public class AlimentoAssembler {

    public Alimento dtoAEntidad(AlimentoDTO alimentoDTO, Usuario usuario) {
        return new Alimento(
                alimentoDTO.getNombre(),
                alimentoDTO.getCalorias(),
                alimentoDTO.getProteinas(),
                alimentoDTO.getCarbohidratos(),
                alimentoDTO.getGrasas(),
                usuario
        );
    }

    public AlimentoDTO entidadADTO(Alimento alimento) {
        AlimentoDTO alimentoDTO = new AlimentoDTO();
        alimentoDTO.setIdAlimento(alimento.getIdAlimento());
        alimentoDTO.setNombre(alimento.getNombre());
        alimentoDTO.setCalorias(alimento.getCalorias());
        alimentoDTO.setProteinas(alimento.getProteinas());
        alimentoDTO.setCarbohidratos(alimento.getCarbohidratos());
        alimentoDTO.setGrasas(alimento.getGrasas());
        return alimentoDTO;
    }
}
