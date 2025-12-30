package trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos;

public class PropiedadesNegativasException extends RuntimeException {

    public PropiedadesNegativasException() {

        super("Las propiedades de los alimentos no pueden ser negativas.");
    }
}
