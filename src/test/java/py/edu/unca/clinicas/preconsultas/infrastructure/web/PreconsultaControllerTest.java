package py.edu.unca.clinicas.preconsultas.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.unca.clinicas.preconsultas.application.PreconsultaService;
import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;

@WebMvcTest(PreconsultaController.class)
class PreconsultaControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PreconsultaService service;

    @Test
    void crearDevuelve201() throws Exception {
        when(service.crear(any())).thenAnswer(inv -> inv.<Preconsulta>getArgument(0)
                .con(1L, LocalDateTime.of(2026, 9, 30, 9, 15)));

        mvc.perform(post("/api/preconsultas").contentType(MediaType.APPLICATION_JSON).content("""
                        {"pacienteId":1,"presionSistolica":120,"presionDiastolica":80,
                         "frecuenciaCardiaca":72,"temperatura":36.5}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fechaHora").value("2026-09-30T09:15:00"))
                .andExpect(jsonPath("$.temperatura").value(36.5));
    }

    @Test
    void signosVitalesFueraDeRangoDevuelve400() throws Exception {
        mvc.perform(post("/api/preconsultas").contentType(MediaType.APPLICATION_JSON).content("""
                        {"pacienteId":1,"presionSistolica":120,"presionDiastolica":80,
                         "frecuenciaCardiaca":500,"temperatura":52}"""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.frecuenciaCardiaca").exists())
                .andExpect(jsonPath("$.errores.temperatura").exists());
    }

    @Test
    void presionIncoherenteDevuelve422() throws Exception {
        mvc.perform(post("/api/preconsultas").contentType(MediaType.APPLICATION_JSON).content("""
                        {"pacienteId":1,"presionSistolica":70,"presionDiastolica":90,
                         "frecuenciaCardiaca":72,"temperatura":36.5}"""))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void listarPorPaciente() throws Exception {
        when(service.listarPorPaciente(1L)).thenReturn(List.of(
                new Preconsulta(2L, 1L, LocalDateTime.of(2026, 9, 30, 9, 0), 120, 80, 72, new BigDecimal("36.5"))));

        mvc.perform(get("/api/pacientes/1/preconsultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].pacienteId").value(1));
    }
}
