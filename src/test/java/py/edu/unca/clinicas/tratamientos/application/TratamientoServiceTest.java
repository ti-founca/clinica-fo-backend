package py.edu.unca.clinicas.tratamientos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import py.edu.unca.clinicas.materias.domain.MateriaRepository;
import py.edu.unca.clinicas.pacientes.domain.PacienteRepository;
import py.edu.unca.clinicas.procedimientos.domain.ProcedimientoRepository;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;
import py.edu.unca.clinicas.shared.domain.ReglaDeNegocioException;
import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;
import py.edu.unca.clinicas.tratamientos.domain.TratamientoRepository;

@ExtendWith(MockitoExtension.class)
class TratamientoServiceTest {

    @Mock
    TratamientoRepository repository;

    @Mock
    PacienteRepository pacienteRepository;

    @Mock
    ProcedimientoRepository procedimientoRepository;

    @Mock
    MateriaRepository materiaRepository;

    @InjectMocks
    TratamientoService service;

    private final Tratamiento datos = new Tratamiento(null, 1L, 2L, 3L, LocalDate.of(2026, 9, 1), null);

    @Test
    void crearGuardaTratamientoEnCurso() {
        when(pacienteRepository.existePorId(1L)).thenReturn(true);
        when(procedimientoRepository.existePorId(2L)).thenReturn(true);
        when(materiaRepository.existePorId(3L)).thenReturn(true);
        when(repository.guardar(datos)).thenReturn(datos.conId(10L));

        assertThat(service.crear(datos).id()).isEqualTo(10L);
    }

    @Test
    void crearConProcedimientoInexistenteFalla() {
        when(pacienteRepository.existePorId(1L)).thenReturn(true);
        when(procedimientoRepository.existePorId(2L)).thenReturn(false);

        assertThatThrownBy(() -> service.crear(datos))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Procedimiento");
        verify(repository, never()).guardar(any());
    }

    @Test
    void crearConMateriaInexistenteFalla() {
        when(pacienteRepository.existePorId(1L)).thenReturn(true);
        when(procedimientoRepository.existePorId(2L)).thenReturn(true);
        when(materiaRepository.existePorId(3L)).thenReturn(false);

        assertThatThrownBy(() -> service.crear(datos))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Materia");
        verify(repository, never()).guardar(any());
    }

    @Test
    void listarPorPacienteInexistenteFalla() {
        when(pacienteRepository.existePorId(7L)).thenReturn(false);

        assertThatThrownBy(() -> service.listarPorPaciente(7L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void fechaFinNoPuedeSerAnteriorALaDeInicio() {
        assertThatThrownBy(() -> new Tratamiento(null, 1L, 2L, 3L,
                LocalDate.of(2026, 9, 10), LocalDate.of(2026, 9, 9)))
                .isInstanceOf(ReglaDeNegocioException.class);
    }
}
