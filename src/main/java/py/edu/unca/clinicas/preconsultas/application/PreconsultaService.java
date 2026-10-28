package py.edu.unca.clinicas.preconsultas.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;
import py.edu.unca.clinicas.preconsultas.domain.PreconsultaRepository;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

/** Casos de uso de preconsultas. */
@Service
@Transactional
public class PreconsultaService {

    private final PreconsultaRepository repository;
    private final PacienteRepository pacienteRepository;

    public PreconsultaService(PreconsultaRepository repository, PacienteRepository pacienteRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
    }

    /** Si no se indica fecha y hora, se toma el momento actual. */
    public Preconsulta crear(Preconsulta datos) {
        verificarPaciente(datos.pacienteId());
        LocalDateTime fechaHora = datos.fechaHora() != null ? datos.fechaHora() : LocalDateTime.now();
        return repository.guardar(datos.con(null, fechaHora));
    }

    @Transactional(readOnly = true)
    public Preconsulta obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Preconsulta", id));
    }

    @Transactional(readOnly = true)
    public List<Preconsulta> listar() {
        return repository.listar();
    }

    @Transactional(readOnly = true)
    public List<Preconsulta> listarPorPaciente(Long pacienteId) {
        verificarPaciente(pacienteId);
        return repository.listarPorPaciente(pacienteId);
    }

    /** Si no se indica fecha y hora, se conserva la registrada. */
    public Preconsulta actualizar(Long id, Preconsulta datos) {
        Preconsulta actual = obtener(id);
        verificarPaciente(datos.pacienteId());
        LocalDateTime fechaHora = datos.fechaHora() != null ? datos.fechaHora() : actual.fechaHora();
        return repository.guardar(datos.con(id, fechaHora));
    }

    public void eliminar(Long id) {
        obtener(id);
        repository.eliminar(id);
    }

    private void verificarPaciente(Long pacienteId) {
        if (!pacienteRepository.existePorId(pacienteId)) {
            throw new RecursoNoEncontradoException("Paciente", pacienteId);
        }
    }
}
