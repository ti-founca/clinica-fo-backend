package py.edu.unca.clinicas.preconsultas.infrastructure.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;

@Schema(name = "Preconsulta", description = "Preconsulta registrada")
record PreconsultaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "1") Long pacienteId,
        @Schema(description = "Hora local", example = "2026-09-30T09:15:00") LocalDateTime fechaHora,
        @Schema(description = "mmHg", example = "120") int presionSistolica,
        @Schema(description = "mmHg", example = "80") int presionDiastolica,
        @Schema(description = "Latidos por minuto", example = "72") int frecuenciaCardiaca,
        @Schema(description = "°C", example = "36.5") BigDecimal temperatura) {

    static PreconsultaResponse desde(Preconsulta p) {
        return new PreconsultaResponse(p.id(), p.pacienteId(), p.fechaHora(), p.presionSistolica(),
                p.presionDiastolica(), p.frecuenciaCardiaca(), p.temperatura());
    }
}
