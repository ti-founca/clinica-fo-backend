package py.edu.unca.clinicas.procedimientos.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.unca.clinicas.procedimientos.application.ProcedimientoService;
import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;

@WebMvcTest(ProcedimientoController.class)
class ProcedimientoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    ProcedimientoService service;

    @Test
    void crearDevuelve201ConDescripcionNormalizada() throws Exception {
        when(service.crear(any())).thenAnswer(inv -> inv.<Procedimiento>getArgument(0).conId(1L));

        mvc.perform(post("/api/procedimientos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"  Extracción dentaria \"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/procedimientos/1"))
                .andExpect(jsonPath("$.descripcion").value("Extracción dentaria"));
    }

    @Test
    void descripcionVaciaDevuelve400() throws Exception {
        mvc.perform(post("/api/procedimientos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.descripcion").exists());
    }
}
