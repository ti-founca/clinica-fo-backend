package py.edu.unca.clinicas.pacientes.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.unca.clinicas.pacientes.domain.Paciente;
import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

/** Casos de uso de pacientes. */
@Service
@Transactional
public class PacienteService {

    private final PacienteRepository repository;

    public PacienteService(PacienteRepository repository) {
        this.repository = repository;
    }

    public Paciente crear(Paciente datos) {
        validarDocumentoUnico(datos.numeroDocumento(), null);
        return repository.guardar(datos.conId(null));
    }

    @Transactional(readOnly = true)
    public Paciente obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente", id));
    }

    @Transactional(readOnly = true)
    public List<Paciente> listar() {
        return repository.listar();
    }

    public Paciente actualizar(Long id, Paciente datos) {
        verificarExistencia(id);
        validarDocumentoUnico(datos.numeroDocumento(), id);
        return repository.guardar(datos.conId(id));
    }

    public void eliminar(Long id) {
        verificarExistencia(id);
        repository.eliminar(id);
    }

    private void verificarExistencia(Long id) {
        if (!repository.existePorId(id)) {
            throw new RecursoNoEncontradoException("Paciente", id);
        }
    }

    private void validarDocumentoUnico(String numeroDocumento, Long excluirId) {
        if (repository.existeDocumento(numeroDocumento, excluirId)) {
            throw new ConflictoException("Ya existe un paciente con el documento " + numeroDocumento);
        }
    }
}
