package py.edu.unca.clinicas.procedimientos.application;

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

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;
import py.edu.unca.clinicas.procedimientos.domain.ProcedimientoRepository;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

@ExtendWith(MockitoExtension.class)
class ProcedimientoServiceTest {

    @Mock
    ProcedimientoRepository repository;

    @InjectMocks
    ProcedimientoService service;

    private final Procedimiento datos = new Procedimiento(null, "Extracción dentaria");

    @Test
    void crearRechazaDescripcionDuplicada() {
        when(repository.existeDescripcion("Extracción dentaria", null)).thenReturn(true);

        assertThatThrownBy(() -> service.crear(datos)).isInstanceOf(ConflictoException.class);
        verify(repository, never()).guardar(any());
    }

    @Test
    void actualizarPermiteConservarLaMismaDescripcion() {
        when(repository.existePorId(1L)).thenReturn(true);
        when(repository.existeDescripcion("Extracción dentaria", 1L)).thenReturn(false);
        when(repository.guardar(datos.conId(1L))).thenReturn(datos.conId(1L));

        assertThat(service.actualizar(1L, datos).id()).isEqualTo(1L);
    }

    @Test
    void eliminarInexistenteFalla() {
        when(repository.existePorId(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar(99L)).isInstanceOf(RecursoNoEncontradoException.class);
        verify(repository, never()).eliminar(any());
    }
}
