package py.edu.unca.clinicas.shared.domain;

/** Violación de una invariante del dominio. */
public class ReglaDeNegocioException extends RuntimeException {

    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
