package trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos;

public class AlimentoDuplicadoException extends RuntimeException {

    public AlimentoDuplicadoException() {
      super("Ya existe un alimento con este nombre.");
    }
}
