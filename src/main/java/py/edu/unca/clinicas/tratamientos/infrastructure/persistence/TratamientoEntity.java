package py.edu.unca.clinicas.tratamientos.infrastructure.persistence;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.unca.clinicas.tratamientos.domain.Tratamiento;

/**
 * Paciente, procedimiento y materia se referencian solo por id (sin @ManyToOne) para mantener los
 * slices desacoplados; la integridad la garantizan las FK de la base de datos.
 */
@Entity
@Table(name = "tratamientos")
class TratamientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "procedimiento_id", nullable = false)
    private Long procedimientoId;

    @Column(name = "materia_id", nullable = false)
    private Long materiaId;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    protected TratamientoEntity() {
    }

    static TratamientoEntity desde(Tratamiento t) {
        TratamientoEntity e = new TratamientoEntity();
        e.id = t.id();
        e.pacienteId = t.pacienteId();
        e.procedimientoId = t.procedimientoId();
        e.materiaId = t.materiaId();
        e.fechaInicio = t.fechaInicio();
        e.fechaFin = t.fechaFin();
        return e;
    }

    Tratamiento aDominio() {
        return new Tratamiento(id, pacienteId, procedimientoId, materiaId, fechaInicio, fechaFin);
    }
}
