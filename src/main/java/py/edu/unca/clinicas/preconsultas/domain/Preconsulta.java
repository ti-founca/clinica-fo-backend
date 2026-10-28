package py.edu.unca.clinicas.preconsultas.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import py.edu.unca.clinicas.shared.domain.ReglaDeNegocioException;

/**
 * Toma de signos vitales previa a la consulta.
 * La presión arterial se registra en mmHg, la frecuencia cardíaca en lpm y la temperatura en °C.
 */
public record Preconsulta(
        Long id,
        Long pacienteId,
        LocalDateTime fechaHora,
        int presionSistolica,
        int presionDiastolica,
        int frecuenciaCardiaca,
        BigDecimal temperatura) {

    public Preconsulta {
        if (presionSistolica <= presionDiastolica) {
            throw new ReglaDeNegocioException("La presión sistólica debe ser mayor que la diastólica");
        }
    }

    public Preconsulta con(Long nuevoId, LocalDateTime nuevaFechaHora) {
        return new Preconsulta(nuevoId, pacienteId, nuevaFechaHora, presionSistolica, presionDiastolica,
                frecuenciaCardiaca, temperatura);
    }
}
