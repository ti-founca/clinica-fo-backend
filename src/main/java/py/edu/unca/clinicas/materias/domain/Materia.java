package py.edu.unca.clinicas.materias.domain;

/** Asignatura de la carrera. {@code id} es nulo mientras no haya sido persistida. */
public record Materia(Long id, String descripcion) {

    public Materia conId(Long nuevoId) {
        return new Materia(nuevoId, descripcion);
    }
}
