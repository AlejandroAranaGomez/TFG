package trabajo.aplicacionSaludable.Dtos;

import java.util.List;

public class RespuestaEjerciciosDTO {

    private int total;
    private int page;
    private int limit;
    private List<ApiEjercicioDTO> data;

    public List<ApiEjercicioDTO> getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }
}
