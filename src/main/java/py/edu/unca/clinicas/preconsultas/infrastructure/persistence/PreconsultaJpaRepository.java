package py.edu.unca.clinicas.preconsultas.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface PreconsultaJpaRepository extends JpaRepository<PreconsultaEntity, Long> {

    List<PreconsultaEntity> findByPacienteIdOrderByFechaHoraDesc(Long pacienteId);
}
