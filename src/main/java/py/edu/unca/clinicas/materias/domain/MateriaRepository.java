package py.edu.unca.clinicas.materias.domain;

import java.util.List;
import java.util.Optional;

/** Puerto de persistencia de materias; lo implementa la capa de infraestructura. */
public interface MateriaRepository {

    Materia guardar(Materia materia);

    Optional<Materia> buscarPorId(Long id);

    List<Materia> listar();

    boolean existePorId(Long id);

    /** Indica si la descripción (sin distinguir mayúsculas) ya pertenece a otra materia distinta de {@code excluirId} (puede ser nulo). */
    boolean existeDescripcion(String descripcion, Long excluirId);

    void eliminar(Long id);
}
