package py.edu.unca.clinicas.shared.domain;

/** Operación que entra en conflicto con el estado actual de los datos (p. ej. duplicados). */
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
