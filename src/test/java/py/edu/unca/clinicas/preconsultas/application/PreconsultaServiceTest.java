package py.edu.unca.clinicas.preconsultas.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;
import py.edu.unca.clinicas.preconsultas.domain.PreconsultaRepository;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;
import py.edu.unca.clinicas.shared.domain.ReglaDeNegocioException;

@ExtendWith(MockitoExtension.class)
class PreconsultaServiceTest {

    @Mock
    PreconsultaRepository repository;

    @Mock
    PacienteRepository pacienteRepository;

    @InjectMocks
    PreconsultaService service;

    private static Preconsulta preconsulta(Long id, LocalDateTime fechaHora) {
        return new Preconsulta(id, 1L, fechaHora, 120, 80, 72, new BigDecimal("36.5"));
    }

    @Test
    void crearSinFechaUsaElMomentoActual() {
        when(pacienteRepository.existePorId(1L)).thenReturn(true);
        when(repository.guardar(any())).thenAnswer(inv -> inv.<Preconsulta>getArgument(0).con(10L, inv.<Preconsulta>getArgument(0).fechaHora()));

        Preconsulta creada = service.crear(preconsulta(null, null));

        assertThat(creada.id()).isEqualTo(10L);
        assertThat(creada.fechaHora()).isNotNull();
    }

    @Test
    void crearParaPacienteInexistenteFalla() {
        when(pacienteRepository.existePorId(1L)).thenReturn(false);

        assertThatThrownBy(() -> service.crear(preconsulta(null, null)))
                .isInstanceOf(RecursoNoEncontradoException.class);
        verify(repository, never()).guardar(any());
    }

    @Test
    void actualizarSinFechaConservaLaRegistrada() {
        LocalDateTime original = LocalDateTime.of(2026, 9, 1, 8, 30);
        when(repository.buscarPorId(5L)).thenReturn(Optional.of(preconsulta(5L, original)));
        when(pacienteRepository.existePorId(1L)).thenReturn(true);
        when(repository.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.actualizar(5L, preconsulta(null, null)).fechaHora()).isEqualTo(original);
    }

    @Test
    void listarPorPacienteInexistenteFalla() {
        when(pacienteRepository.existePorId(7L)).thenReturn(false);

        assertThatThrownBy(() -> service.listarPorPaciente(7L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void sistolicaDebeSerMayorQueDiastolica() {
        assertThatThrownBy(() -> new Preconsulta(null, 1L, null, 80, 80, 72, new BigDecimal("36.5")))
                .isInstanceOf(ReglaDeNegocioException.class);
    }
}
