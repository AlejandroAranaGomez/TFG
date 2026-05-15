package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Comida;
import trabajo.aplicacionSaludable.Dominio.RegistroComidaDiaria;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.RegistroComidaDiariaDTO;

import java.time.LocalDate;

@Component
public class RegistroComidaDiariaAssembler {

    public RegistroComidaDiariaDTO entidadADTO(RegistroComidaDiaria registro) {
        RegistroComidaDiariaDTO dto = new RegistroComidaDiariaDTO();

        dto.setIdRegistroComidaDiaria(registro.getIdRegistroComidaDiaria());
        dto.setFecha(registro.getFecha().toString());

        dto.setCaloriasTotales(registro.getCaloriasTotales());
        dto.setProteinas(registro.getProteinas());
        dto.setCarbohidratos(registro.getCarbohidratos());
        dto.setGrasas(registro.getGrasas());

        return dto;
    }

    public RegistroComidaDiaria dtoAEntidad(Usuario usuario, Comida comida) {
        RegistroComidaDiaria registro = new RegistroComidaDiaria();

        registro.setUsuario(usuario);
        registro.setComida(comida);
        registro.setFecha(LocalDate.now());

        registro.setCaloriasTotales(comida.getCaloriasTotales());
        registro.setProteinas(comida.getProteinas());
        registro.setCarbohidratos(comida.getCarbohidratos());
        registro.setGrasas(comida.getGrasas());

        return registro;
    }

}
