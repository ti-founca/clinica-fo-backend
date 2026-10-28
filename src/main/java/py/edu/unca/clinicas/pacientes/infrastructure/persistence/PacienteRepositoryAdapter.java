package py.edu.unca.clinicas.pacientes.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import py.edu.unca.clinicas.pacientes.domain.Paciente;
import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;

@Repository
class PacienteRepositoryAdapter implements PacienteRepository {

    private final PacienteJpaRepository jpa;

    PacienteRepositoryAdapter(PacienteJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Paciente guardar(Paciente paciente) {
        return jpa.save(PacienteEntity.desde(paciente)).aDominio();
    }

    @Override
    public Optional<Paciente> buscarPorId(Long id) {
        return jpa.findById(id).map(PacienteEntity::aDominio);
    }

    @Override
    public List<Paciente> listar() {
        return jpa.findAll(Sort.by("apellidos", "nombres")).stream()
                .map(PacienteEntity::aDominio)
                .toList();
    }

    @Override
    public boolean existePorId(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public boolean existeDocumento(String numeroDocumento, Long excluirId) {
        return excluirId == null
                ? jpa.existsByNumeroDocumento(numeroDocumento)
                : jpa.existsByNumeroDocumentoAndIdNot(numeroDocumento, excluirId);
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
        // Forzar el DELETE ahora para que una violación de FK se reporte dentro del caso de uso.
        jpa.flush();
    }
}
