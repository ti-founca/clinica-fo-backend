package py.edu.unca.clinicas.tratamientos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.unca.clinicas.materias.domain.MateriaRepository;
import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.procedimientos.domain.ProcedimientoRepository;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;
import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;
import py.edu.unca.clinicas.tratamientos.domain.TratamientoRepository;

/** Casos de uso de tratamientos. */
@Service
@Transactional
public class TratamientoService {

    private final TratamientoRepository repository;
    private final PacienteRepository pacienteRepository;
    private final ProcedimientoRepository procedimientoRepository;
    private final MateriaRepository materiaRepository;

    public TratamientoService(TratamientoRepository repository, PacienteRepository pacienteRepository,
            ProcedimientoRepository procedimientoRepository, MateriaRepository materiaRepository) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.procedimientoRepository = procedimientoRepository;
        this.materiaRepository = materiaRepository;
    }

    public Tratamiento crear(Tratamiento datos) {
        verificarReferencias(datos);
        return repository.guardar(datos.conId(null));
    }

    @Transactional(readOnly = true)
    public Tratamiento obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tratamiento", id));
    }

    @Transactional(readOnly = true)
    public List<Tratamiento> listar() {
        return repository.listar();
    }

    @Transactional(readOnly = true)
    public List<Tratamiento> listarPorPaciente(Long pacienteId) {
        verificarPaciente(pacienteId);
        return repository.listarPorPaciente(pacienteId);
    }

    public Tratamiento actualizar(Long id, Tratamiento datos) {
        verificarExistencia(id);
        verificarReferencias(datos);
        return repository.guardar(datos.conId(id));
    }

    public void eliminar(Long id) {
        verificarExistencia(id);
        repository.eliminar(id);
    }

    private void verificarExistencia(Long id) {
        if (!repository.existePorId(id)) {
            throw new RecursoNoEncontradoException("Tratamiento", id);
        }
    }

    private void verificarReferencias(Tratamiento datos) {
        verificarPaciente(datos.pacienteId());
        if (!procedimientoRepository.existePorId(datos.procedimientoId())) {
            throw new RecursoNoEncontradoException("Procedimiento", datos.procedimientoId());
        }
        if (!materiaRepository.existePorId(datos.materiaId())) {
            throw new RecursoNoEncontradoException("Materia", datos.materiaId());
        }
    }

    private void verificarPaciente(Long pacienteId) {
        if (!pacienteRepository.existePorId(pacienteId)) {
            throw new RecursoNoEncontradoException("Paciente", pacienteId);
        }
    }
}
