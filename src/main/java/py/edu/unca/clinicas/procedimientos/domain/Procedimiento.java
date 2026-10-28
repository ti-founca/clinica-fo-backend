package py.edu.unca.clinicas.procedimientos.domain;

/** Procedimiento (tipo de tratamiento) que puede recibir un paciente. {@code id} es nulo mientras no haya sido persistido. */
public record Procedimiento(Long id, String descripcion) {

    public Procedimiento conId(Long nuevoId) {
        return new Procedimiento(nuevoId, descripcion);
    }
}
