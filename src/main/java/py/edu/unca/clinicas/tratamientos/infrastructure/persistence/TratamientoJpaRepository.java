package py.edu.unca.clinicas.tratamientos.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface TratamientoJpaRepository extends JpaRepository<TratamientoEntity, Long> {

    List<TratamientoEntity> findByPacienteIdOrderByFechaInicioDescIdDesc(Long pacienteId);
}
