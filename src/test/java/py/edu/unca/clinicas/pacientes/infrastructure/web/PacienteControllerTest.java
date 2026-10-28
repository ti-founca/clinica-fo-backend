package py.edu.unca.clinicas.pacientes.infrastructure.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
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

import py.edu.unca.clinicas.pacientes.application.PacienteService;
import py.edu.unca.clinicas.pacientes.domain.Paciente;
import py.edu.unca.clinicas.shared.domain.ConflictoException;
import py.edu.unca.clinicas.shared.domain.RecursoNoEncontradoException;

@WebMvcTest(PacienteController.class)
class PacienteControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PacienteService service;

    private static final String JSON = """
            {"nombres":"Ana","apellidos":"Pérez","numeroDocumento":"1234567",
             "direccion":"Calle 1","barrio":"Centro","ciudad":"Asunción"}""";

    @Test
    void crearDevuelve201ConLocation() throws Exception {
        when(service.crear(any())).thenAnswer(inv -> inv.<Paciente>getArgument(0).conId(1L));

        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/pacientes/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.numeroDocumento").value("1234567"));
    }

    @Test
    void crearConDatosInvalidosDevuelve400() throws Exception {
        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON).content("{\"nombres\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.nombres").exists())
                .andExpect(jsonPath("$.errores.numeroDocumento").exists());
    }

    @Test
    void documentoDuplicadoDevuelve409() throws Exception {
        when(service.crear(any())).thenThrow(new ConflictoException("duplicado"));

        mvc.perform(post("/api/pacientes").contentType(MediaType.APPLICATION_JSON).content(JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void pacienteInexistenteDevuelve404() throws Exception {
        when(service.obtener(9L)).thenThrow(new RecursoNoEncontradoException("Paciente", 9L));

        mvc.perform(get("/api/pacientes/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Paciente con id 9 no encontrado"));
    }

    @Test
    void corsPermiteElOrigenDelFrontend() throws Exception {
        mvc.perform(options("/api/pacientes")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    void corsRechazaOrigenesNoPermitidos() throws Exception {
        mvc.perform(options("/api/pacientes")
                        .header("Origin", "http://otro-origen.example")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
