package py.edu.unca.clinicas.shared.web;

import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import py.edu.unca.clinicas.materias.application.MateriaService;
import py.edu.unca.clinicas.pacientes.application.PacienteService;
import py.edu.unca.clinicas.preconsultas.application.PreconsultaService;
import py.edu.unca.clinicas.procedimientos.application.ProcedimientoService;
import py.edu.unca.clinicas.tratamientos.application.TratamientoService;

/** Verifica que la especificación OpenAPI se genere con los endpoints y la documentación esperada. */
@WebMvcTest
@Import(OpenApiConfig.class)
@ImportAutoConfiguration({ SpringDocConfiguration.class, SpringDocConfigProperties.class,
        SpringDocWebMvcConfiguration.class, SwaggerConfig.class, SwaggerUiConfigProperties.class,
        SwaggerUiOAuthProperties.class })
class OpenApiDocumentacionTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PacienteService pacienteService;

    @MockitoBean
    PreconsultaService preconsultaService;

    @MockitoBean
    ProcedimientoService procedimientoService;

    @MockitoBean
    MateriaService materiaService;

    @MockitoBean
    TratamientoService tratamientoService;

    @Test
    void especificacionDocumentaLosEndpoints() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Clínicas – API de Preconsultas"))
                .andExpect(jsonPath("$.tags[*].name").value(hasItems("Pacientes", "Preconsultas",
                        "Procedimientos", "Materias", "Tratamientos")))
                .andExpect(jsonPath("$.paths['/api/pacientes'].post.summary").value("Registrar paciente"))
                .andExpect(jsonPath("$.paths['/api/pacientes'].post.responses['201']").exists())
                .andExpect(jsonPath("$.paths['/api/pacientes'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/pacientes/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/api/pacientes/{id}'].get.responses['200'].content['application/json']").exists())
                .andExpect(jsonPath("$.paths['/api/pacientes/{pacienteId}/preconsultas'].get.tags[0]")
                        .value("Preconsultas"))
                .andExpect(jsonPath("$.paths['/api/preconsultas'].post.responses['422']").exists())
                .andExpect(jsonPath("$.components.schemas.PreconsultaRequest.properties.temperatura.maximum")
                        .value(45.0))
                .andExpect(jsonPath("$.components.schemas.Paciente.properties.numeroDocumento.example")
                        .value("1234567"))
                .andExpect(jsonPath("$.paths['/api/procedimientos'].post.responses['409']").exists())
                .andExpect(jsonPath("$.paths['/api/materias/{id}'].delete.responses['204']").exists())
                .andExpect(jsonPath("$.paths['/api/pacientes/{pacienteId}/tratamientos'].get.tags[0]")
                        .value("Tratamientos"))
                .andExpect(jsonPath("$.paths['/api/tratamientos'].post.responses['422']").exists());
    }

    @Test
    void swaggerUiDisponible() throws Exception {
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
}
