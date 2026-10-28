package py.edu.unca.clinicas.tratamientos.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.unca.clinicas.tratamientos.application.TratamientoService;
import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;

@WebMvcTest(TratamientoController.class)
class TratamientoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    TratamientoService service;

    @Test
    void crearDevuelve201() throws Exception {
        when(service.crear(any())).thenAnswer(inv -> inv.<Tratamiento>getArgument(0).conId(1L));

        mvc.perform(post("/api/tratamientos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"pacienteId":1,"procedimientoId":2,"materiaId":3,
                         "fechaInicio":"2026-09-01","fechaFin":"2026-09-30"}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fechaInicio").value("2026-09-01"))
                .andExpect(jsonPath("$.fechaFin").value("2026-09-30"));
    }

    @Test
    void faltanDatosObligatoriosDevuelve400() throws Exception {
        mvc.perform(post("/api/tratamientos").contentType(MediaType.APPLICATION_JSON).content("{\"pacienteId\":1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.procedimientoId").exists())
                .andExpect(jsonPath("$.errores.materiaId").exists())
                .andExpect(jsonPath("$.errores.fechaInicio").exists());
    }

    @Test
    void fechasIncoherentesDevuelve422() throws Exception {
        mvc.perform(post("/api/tratamientos").contentType(MediaType.APPLICATION_JSON).content("""
                        {"pacienteId":1,"procedimientoId":2,"materiaId":3,
                         "fechaInicio":"2026-09-10","fechaFin":"2026-09-01"}"""))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void listarPorPaciente() throws Exception {
        when(service.listarPorPaciente(1L)).thenReturn(List.of(
                new Tratamiento(5L, 1L, 2L, 3L, LocalDate.of(2026, 9, 1), null)));

        mvc.perform(get("/api/pacientes/1/tratamientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(5))
                .andExpect(jsonPath("$[0].fechaFin").isEmpty());
    }
}
