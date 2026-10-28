package py.edu.unca.clinicas.tratamientos.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;
import py.edu.unca.clinicas.tratamientos.domain.TratamientoRepository;

@Repository
class TratamientoRepositoryAdapter implements TratamientoRepository {

    private final TratamientoJpaRepository jpa;

    TratamientoRepositoryAdapter(TratamientoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Tratamiento guardar(Tratamiento tratamiento) {
        return jpa.save(TratamientoEntity.desde(tratamiento)).aDominio();
    }

    @Override
    public Optional<Tratamiento> buscarPorId(Long id) {
        return jpa.findById(id).map(TratamientoEntity::aDominio);
    }

    @Override
    public List<Tratamiento> listar() {
        return jpa.findAll(Sort.by(Sort.Order.desc("fechaInicio"), Sort.Order.desc("id"))).stream()
                .map(TratamientoEntity::aDominio)
                .toList();
    }

    @Override
    public List<Tratamiento> listarPorPaciente(Long pacienteId) {
        return jpa.findByPacienteIdOrderByFechaInicioDescIdDesc(pacienteId).stream()
                .map(TratamientoEntity::aDominio)
                .toList();
    }

    @Override
    public boolean existePorId(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }
}
