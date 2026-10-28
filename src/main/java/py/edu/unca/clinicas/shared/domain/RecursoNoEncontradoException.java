package py.edu.unca.clinicas.shared.domain;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super("%s con id %d no encontrado".formatted(recurso, id));
    }
}
