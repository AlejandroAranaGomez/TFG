package trabajo.aplicacionSaludable.Excepciones.ExcepcionesAlimentos;

public class AlimentoDeOtroUsuarioException extends RuntimeException {

    public AlimentoDeOtroUsuarioException() {
        super("Este alimento pertenece a otro usuario.");
    }
}
