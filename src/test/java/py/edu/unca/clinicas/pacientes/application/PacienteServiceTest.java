package py.edu.unca.clinicas.pacientes.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import py.edu.unca.clinicas.pacientes.domain.Paciente;
import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    PacienteRepository repository;

    @InjectMocks
    PacienteService service;

    private final Paciente datos = new Paciente(null, "Ana", "Pérez", "1234567", "Calle 1", "Centro", "Asunción");

    @Test
    void crearRechazaDocumentoDuplicado() {
        when(repository.existeDocumento("1234567", null)).thenReturn(true);

        assertThatThrownBy(() -> service.crear(datos)).isInstanceOf(ConflictoException.class);
        verify(repository, never()).guardar(any());
    }

    @Test
    void crearGuardaPaciente() {
        when(repository.guardar(datos)).thenReturn(datos.conId(1L));

        assertThat(service.crear(datos).id()).isEqualTo(1L);
    }

    @Test
    void actualizarPermiteConservarElMismoDocumento() {
        when(repository.existePorId(1L)).thenReturn(true);
        when(repository.existeDocumento("1234567", 1L)).thenReturn(false);
        when(repository.guardar(datos.conId(1L))).thenReturn(datos.conId(1L));

        assertThat(service.actualizar(1L, datos).id()).isEqualTo(1L);
    }

    @Test
    void eliminarPacienteInexistenteFalla() {
        when(repository.existePorId(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(99L)).isInstanceOf(RecursoNoEncontradoException.class);
        verify(repository, never()).eliminar(any());
    }
}
