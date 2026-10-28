package py.edu.unca.clinicas.materias.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.edu.unca.clinicas.materias.domain.Materia;
import py.edu.unca.clinicas.materias.domain.MateriaRepository;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

/** Casos de uso de materias. */
@Service
@Transactional
public class MateriaService {

    private final MateriaRepository repository;

    public MateriaService(MateriaRepository repository) {
        this.repository = repository;
    }

    public Materia crear(Materia datos) {
        validarDescripcionUnica(datos.descripcion(), null);
        return repository.guardar(datos.conId(null));
    }

    @Transactional(readOnly = true)
    public Materia obtener(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Materia", id));
    }

    @Transactional(readOnly = true)
    public List<Materia> listar() {
        return repository.listar();
    }

    public Materia actualizar(Long id, Materia datos) {
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
            throw new RecursoNoEncontradoException("Materia", id);
        }
    }

    private void validarDescripcionUnica(String descripcion, Long excluirId) {
        if (repository.existeDescripcion(descripcion, excluirId)) {
            throw new ConflictoException("Ya existe una materia con la descripción " + descripcion);
        }
    }
}
