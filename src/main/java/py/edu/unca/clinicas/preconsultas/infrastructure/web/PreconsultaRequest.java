package py.edu.unca.clinicas.preconsultas.infrastructure.web;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;

/** Los rangos acotan valores fisiológicamente plausibles para detectar errores de carga. */
@Schema(name = "PreconsultaRequest", description = "Signos vitales a registrar en la preconsulta")
record PreconsultaRequest(
        @Schema(description = "Id del paciente", example = "1")
        @NotNull Long pacienteId,
        @Schema(description = "Fecha y hora de la toma (hora local). Opcional: al crear se usa el momento "
                + "actual y al actualizar se conserva la registrada.", example = "2026-09-30T09:15:00")
        @PastOrPresent LocalDateTime fechaHora,
        @Schema(description = "Presión arterial sistólica en mmHg; debe ser mayor que la diastólica", example = "120")
        @NotNull @Min(50) @Max(300) Integer presionSistolica,
        @Schema(description = "Presión arterial diastólica en mmHg", example = "80")
        @NotNull @Min(20) @Max(200) Integer presionDiastolica,
        @Schema(description = "Frecuencia cardíaca en latidos por minuto", example = "72")
        @NotNull @Min(20) @Max(250) Integer frecuenciaCardiaca,
        @Schema(description = "Temperatura corporal en °C, con un decimal", example = "36.5")
        @NotNull @DecimalMin("30.0") @DecimalMax("45.0") @Digits(integer = 2, fraction = 1) BigDecimal temperatura) {

    Preconsulta aDominio() {
        return new Preconsulta(null, pacienteId, fechaHora, presionSistolica, presionDiastolica,
                frecuenciaCardiaca, temperatura);
    }
}
