package py.edu.unca.clinicas.shared.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

/** Metadatos de la documentación OpenAPI (Swagger UI en /swagger-ui.html). */
@Configuration
public class OpenApiConfig {

    public static final String TAG_PACIENTES = "Pacientes";
    public static final String TAG_PRECONSULTAS = "Preconsultas";
    public static final String TAG_PROCEDIMIENTOS = "Procedimientos";
    public static final String TAG_MATERIAS = "Materias";
    public static final String TAG_TRATAMIENTOS = "Tratamientos";

    @Bean
    OpenAPI clinicasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clínicas – API de Preconsultas")
                        .version("v1")
                        .description("""
                                Registro de pacientes y de las preconsultas (toma de signos vitales \
                                previa a la consulta) y de los tratamientos que reciben. Los errores se devuelven en formato \
                                `application/problem+json` (RFC 9457)."""))
                .addTagsItem(new Tag().name(TAG_PACIENTES).description("Datos básicos de los pacientes"))
                .addTagsItem(new Tag().name(TAG_PRECONSULTAS)
                        .description("Signos vitales: presión arterial, frecuencia cardíaca y temperatura"))
                .addTagsItem(new Tag().name(TAG_PROCEDIMIENTOS)
                        .description("Catálogo de procedimientos (tipos de tratamiento) que puede recibir un paciente"))
                .addTagsItem(new Tag().name(TAG_MATERIAS).description("Catálogo de materias de la carrera"))
                .addTagsItem(new Tag().name(TAG_TRATAMIENTOS)
                        .description("Procedimientos realizados a un paciente en una materia, con fechas de inicio y fin"));
    }
}
