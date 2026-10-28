package py.edu.unca.clinicas.pacientes.domain;

import java.util.List;
import java.util.Optional;

/** Puerto de persistencia de pacientes; lo implementa la capa de infraestructura. */
public interface PacienteRepository {

    Paciente guardar(Paciente paciente);

    Optional<Paciente> buscarPorId(Long id);

    List<Paciente> listar();

    boolean existePorId(Long id);

    /** Indica si el documento ya pertenece a otro paciente distinto de {@code excluirId} (puede ser nulo). */
    boolean existeDocumento(String numeroDocumento, Long excluirId);

    void eliminar(Long id);
}
