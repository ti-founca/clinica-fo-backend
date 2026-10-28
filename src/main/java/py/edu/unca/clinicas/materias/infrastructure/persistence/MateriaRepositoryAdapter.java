package py.edu.unca.clinicas.materias.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import py.edu.unca.clinicas.materias.domain.Materia;
import py.edu.unca.clinicas.materias.domain.MateriaRepository;

@Repository
class MateriaRepositoryAdapter implements MateriaRepository {

    private final MateriaJpaRepository jpa;

    MateriaRepositoryAdapter(MateriaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Materia guardar(Materia materia) {
        return jpa.save(MateriaEntity.desde(materia)).aDominio();
    }

    @Override
    public Optional<Materia> buscarPorId(Long id) {
        return jpa.findById(id).map(MateriaEntity::aDominio);
    }

    @Override
    public List<Materia> listar() {
        return jpa.findAll(Sort.by("descripcion")).stream()
                .map(MateriaEntity::aDominio)
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
