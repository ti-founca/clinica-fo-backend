package py.edu.unca.clinicas.tratamientos.domain;

import java.util.List;
import java.util.Optional;

/** Puerto de persistencia de tratamientos; lo implementa la capa de infraestructura. */
public interface TratamientoRepository {

    Tratamiento guardar(Tratamiento tratamiento);

    Optional<Tratamiento> buscarPorId(Long id);

    List<Tratamiento> listar();

    /** Tratamientos del paciente, del más reciente al más antiguo según la fecha de inicio. */
    List<Tratamiento> listarPorPaciente(Long pacienteId);

    boolean existePorId(Long id);

    void eliminar(Long id);
}
