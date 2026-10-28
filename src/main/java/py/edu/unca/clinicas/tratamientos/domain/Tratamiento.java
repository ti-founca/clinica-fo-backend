package py.edu.unca.clinicas.tratamientos.domain;

import java.time.LocalDate;

import py.edu.unca.clinicas.shared.domain.ReglaDeNegocioException;

/**
 * Procedimiento que recibe un paciente en el marco de una materia de la carrera.
 * {@code fechaFin} es nula mientras el tratamiento está en curso.
 */
public record Tratamiento(
        Long id,
        Long pacienteId,
        Long procedimientoId,
        Long materiaId,
        LocalDate fechaInicio,
        LocalDate fechaFin) {

    public Tratamiento {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            throw new ReglaDeNegocioException("La fecha de finalización no puede ser anterior a la de inicio");
        }
    }

    public Tratamiento conId(Long nuevoId) {
        return new Tratamiento(nuevoId, pacienteId, procedimientoId, materiaId, fechaInicio, fechaFin);
    }
}
