package py.edu.unca.clinicas.tratamientos.infrastructure.web;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;

@Schema(name = "Tratamiento", description = "Tratamiento registrado")
record TratamientoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long pacienteId,
        @Schema(example = "1") Long procedimientoId,
        @Schema(example = "1") Long materiaId,
        @Schema(example = "2026-09-01") LocalDate fechaInicio,
        @Schema(description = "Nula si el tratamiento está en curso", example = "2026-09-30") LocalDate fechaFin) {

    static TratamientoResponse desde(Tratamiento t) {
        return new TratamientoResponse(t.id(), t.pacienteId(), t.procedimientoId(), t.materiaId(),
                t.fechaInicio(), t.fechaFin());
    }
}
