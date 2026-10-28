package py.edu.unca.clinicas.materias.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface MateriaJpaRepository extends JpaRepository<MateriaEntity, Long> {

    boolean existsByDescripcionIgnoreCase(String descripcion);

    boolean existsByDescripcionIgnoreCaseAndIdNot(String descripcion, Long id);
}
