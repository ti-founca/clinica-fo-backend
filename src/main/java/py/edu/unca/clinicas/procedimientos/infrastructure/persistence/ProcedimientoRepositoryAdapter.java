package py.edu.unca.clinicas.procedimientos.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;
import py.edu.unca.clinicas.procedimientos.domain.ProcedimientoRepository;

@Repository
class ProcedimientoRepositoryAdapter implements ProcedimientoRepository {

    private final ProcedimientoJpaRepository jpa;

    ProcedimientoRepositoryAdapter(ProcedimientoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Procedimiento guardar(Procedimiento procedimiento) {
        return jpa.save(ProcedimientoEntity.desde(procedimiento)).aDominio();
    }

    @Override
    public Optional<Procedimiento> buscarPorId(Long id) {
        return jpa.findById(id).map(ProcedimientoEntity::aDominio);
    }

    @Override
    public List<Procedimiento> listar() {
        return jpa.findAll(Sort.by("descripcion")).stream()
                .map(ProcedimientoEntity::aDominio)
                .toList();
    }

    @Override
    public boolean existePorId(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public boolean existeDescripcion(String descripcion, Long excluirId) {
        return excluirId == null
                ? jpa.existsByDescripcionIgnoreCase(descripcion)
                : jpa.existsByDescripcionIgnoreCaseAndIdNot(descripcion, excluirId);
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
        // Forzar el DELETE ahora para que una violación de FK se reporte dentro del caso de uso.
        jpa.flush();
    }
}
