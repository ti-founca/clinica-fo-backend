package py.edu.unca.clinicas.pacientes.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.unca.clinicas.pacientes.domain.Paciente;

@Entity
@Table(name = "pacientes")
class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombres;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(name = "numero_documento", nullable = false, length = 20, unique = true)
    private String numeroDocumento;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String barrio;

    @Column(nullable = false, length = 100)
    private String ciudad;

    protected PacienteEntity() {
    }

    static PacienteEntity desde(Paciente p) {
        PacienteEntity e = new PacienteEntity();
        e.id = p.id();
        e.nombres = p.nombres();
        e.apellidos = p.apellidos();
        e.numeroDocumento = p.numeroDocumento();
        e.direccion = p.direccion();
        e.barrio = p.barrio();
        e.ciudad = p.ciudad();
        return e;
    }

    Paciente aDominio() {
        return new Paciente(id, nombres, apellidos, numeroDocumento, direccion, barrio, ciudad);
    }
}
