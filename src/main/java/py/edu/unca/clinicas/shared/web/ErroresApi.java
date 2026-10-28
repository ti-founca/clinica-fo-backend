package py.edu.unca.clinicas.shared.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.ProblemDetail;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/** Respuestas de error reutilizables para documentar los endpoints. */
public final class ErroresApi {

    private static final String PROBLEM_JSON = "application/problem+json";

    private ErroresApi() {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content(
            mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class),
            examples = @ExampleObject(value = """
                    {"type":"about:blank","title":"Bad Request","status":400,"detail":"Datos inválidos",
                     "instance":"/api/pacientes","errores":{"nombres":"no debe estar vacío"}}""")))
    public @interface DatosInvalidos {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(responseCode = "404", description = "Recurso no encontrado", content = @Content(
            mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class),
            examples = @ExampleObject(value = """
                    {"type":"about:blank","title":"Not Found","status":404,
                     "detail":"Paciente con id 9 no encontrado","instance":"/api/pacientes/9"}""")))
    public @interface NoEncontrado {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(responseCode = "409", description = "Conflicto con los datos existentes", content = @Content(
            mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class)))
    public @interface Conflicto {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponse(responseCode = "422", description = "Regla de negocio violada", content = @Content(
            mediaType = PROBLEM_JSON, schema = @Schema(implementation = ProblemDetail.class),
            examples = @ExampleObject(value = """
                    {"type":"about:blank","title":"Unprocessable Content","status":422,
                     "detail":"La presión sistólica debe ser mayor que la diastólica","instance":"/api/preconsultas"}""")))
    public @interface ReglaDeNegocio {
    }
}
