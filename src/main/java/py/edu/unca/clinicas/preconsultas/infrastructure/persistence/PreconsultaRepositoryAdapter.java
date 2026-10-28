package py.edu.unca.clinicas.preconsultas.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;
import py.edu.unca.clinicas.preconsultas.domain.PreconsultaRepository;

@Repository
class PreconsultaRepositoryAdapter implements PreconsultaRepository {

    private final PreconsultaJpaRepository jpa;

    PreconsultaRepositoryAdapter(PreconsultaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Preconsulta guardar(Preconsulta preconsulta) {
        return jpa.save(PreconsultaEntity.desde(preconsulta)).aDominio();
    }

    @Override
    public Optional<Preconsulta> buscarPorId(Long id) {
        return jpa.findById(id).map(PreconsultaEntity::aDominio);
    }

    @Override
    public List<Preconsulta> listar() {
        return jpa.findAll(Sort.by(Sort.Direction.DESC, "fechaHora")).stream()
                .map(PreconsultaEntity::aDominio)
                .toList();
    }

    @Override
    public List<Preconsulta> listarPorPaciente(Long pacienteId) {
        return jpa.findByPacienteIdOrderByFechaHoraDesc(pacienteId).stream()
                .map(PreconsultaEntity::aDominio)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }
}
