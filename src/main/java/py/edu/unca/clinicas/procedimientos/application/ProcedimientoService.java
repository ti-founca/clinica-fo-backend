package py.edu.unca.clinicas.procedimientos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;
import py.edu.unca.clinicas.procedimientos.domain.ProcedimientoRepository;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

/** Casos de uso de procedimientos. */
@Service
@Transactional
public class ProcedimientoService {

    private final ProcedimientoRepository repository;

    public ProcedimientoService(ProcedimientoRepository repository) {
        this.repository = repository;
    }

    public Procedimiento crear(Procedimiento datos) {
        validarDescripcionUnica(datos.descripcion(), null);
        return repository.guardar(datos.conId(null));
    }

    @Transactional(readOnly = true)
    public Procedimiento obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Procedimiento", id));
    }

    @Transactional(readOnly = true)
    public List<Procedimiento> listar() {
        return repository.listar();
    }

    public Procedimiento actualizar(Long id, Procedimiento datos) {
        verificarExistencia(id);
        validarDescripcionUnica(datos.descripcion(), id);
        return repository.guardar(datos.conId(id));
    }

    public void eliminar(Long id) {
        verificarExistencia(id);
        repository.eliminar(id);
    }

    private void verificarExistencia(Long id) {
        if (!repository.existePorId(id)) {
            throw new RecursoNoEncontradoException("Procedimiento", id);
        }
    }

    private void validarDescripcionUnica(String descripcion, Long excluirId) {
        if (repository.existeDescripcion(descripcion, excluirId)) {
            throw new ConflictoException("Ya existe un procedimiento con la descripción " + descripcion);
        }
    }
}
