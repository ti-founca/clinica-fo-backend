package py.edu.unca.clinicas.procedimientos.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProcedimientoJpaRepository extends JpaRepository<ProcedimientoEntity, Long> {

    boolean existsByDescripcionIgnoreCase(String descripcion);

    boolean existsByDescripcionIgnoreCaseAndIdNot(String descripcion, Long id);
}
