package py.edu.unca.clinicas.materias.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.unca.clinicas.materias.domain.Materia;

@Entity
@Table(name = "materias")
class MateriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String descripcion;

    protected MateriaEntity() {
    }

    static MateriaEntity desde(Materia m) {
        MateriaEntity e = new MateriaEntity();
        e.id = m.id();
        e.descripcion = m.descripcion();
        return e;
    }

    Materia aDominio() {
        return new Materia(id, descripcion);
    }
}
