package py.edu.unca.clinicas.preconsultas.domain;

import java.util.List;
import java.util.Optional;

/** Puerto de persistencia de preconsultas; lo implementa la capa de infraestructura. */
public interface PreconsultaRepository {

    Preconsulta guardar(Preconsulta preconsulta);

    Optional<Preconsulta> buscarPorId(Long id);

    List<Preconsulta> listar();

    /** Preconsultas del paciente, de la más reciente a la más antigua. */
    List<Preconsulta> listarPorPaciente(Long pacienteId);

    void eliminar(Long id);
}
