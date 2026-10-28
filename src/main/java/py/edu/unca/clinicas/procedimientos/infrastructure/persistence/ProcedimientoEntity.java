package py.edu.unca.clinicas.procedimientos.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;

@Entity
@Table(name = "procedimientos")
class ProcedimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descripcion;

    protected ProcedimientoEntity() {
    }

    static ProcedimientoEntity desde(Procedimiento m) {
        ProcedimientoEntity e = new ProcedimientoEntity();
        e.id = m.id();
        e.descripcion = m.descripcion();
        return e;
    }

    Procedimiento aDominio() {
        return new Procedimiento(id, descripcion);
    }
}
