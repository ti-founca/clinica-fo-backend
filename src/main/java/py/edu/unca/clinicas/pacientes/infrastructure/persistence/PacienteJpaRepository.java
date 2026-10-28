package py.edu.unca.clinicas.pacientes.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

interface PacienteJpaRepository extends JpaRepository<PacienteEntity, Long> {

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumentoAndIdNot(String numeroDocumento, Long id);
}
