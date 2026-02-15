package trabajo.aplicacionSaludable.Dtos;

import java.util.List;

public class ApiEjercicioDTO {

    private String id;
    private String name;
    private List<String> primaryMuscles;
    public List<String> getPrimaryMuscles() {
        return primaryMuscles;
    }
    private List<String> images;
    public List<String> getImages() {
        return images;
    }

    public String getId() { return id; }

    public String getName() { return name; }

}
