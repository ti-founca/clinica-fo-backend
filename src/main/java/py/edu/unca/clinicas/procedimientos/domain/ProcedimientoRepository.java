package py.edu.unca.clinicas.procedimientos.domain;

import java.util.List;
import java.util.Optional;

/** Puerto de persistencia de procedimientos; lo implementa la capa de infraestructura. */
public interface ProcedimientoRepository {

    Procedimiento guardar(Procedimiento procedimiento);

    Optional<Procedimiento> buscarPorId(Long id);

    List<Procedimiento> listar();

    boolean existePorId(Long id);

    /** Indica si la descripción (sin distinguir mayúsculas) ya pertenece a otro procedimiento distinto de {@code excluirId} (puede ser nulo). */
    boolean existeDescripcion(String descripcion, Long excluirId);

    void eliminar(Long id);
}
