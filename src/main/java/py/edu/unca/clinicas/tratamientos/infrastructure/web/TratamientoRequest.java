package py.edu.unca.clinicas.tratamientos.infrastructure.web;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;

@Schema(name = "TratamientoRequest", description = "Datos para registrar o actualizar un tratamiento")
record TratamientoRequest(
        @Schema(description = "Id del paciente", example = "1")
        @NotNull Long pacienteId,
        @Schema(description = "Id del procedimiento realizado", example = "1")
        @NotNull Long procedimientoId,
        @Schema(description = "Id de la materia en la que se realiza", example = "1")
        @NotNull Long materiaId,
        @Schema(example = "2026-09-01")
        @NotNull LocalDate fechaInicio,
        @Schema(description = "Opcional mientras el tratamiento está en curso; no puede ser anterior a "
                + "`fechaInicio`", example = "2026-09-30")
        LocalDate fechaFin) {

    Tratamiento aDominio() {
        return new Tratamiento(null, pacienteId, procedimientoId, materiaId, fechaInicio, fechaFin);
    }
}
