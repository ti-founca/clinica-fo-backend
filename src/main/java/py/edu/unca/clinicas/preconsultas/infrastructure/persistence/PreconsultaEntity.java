package py.edu.unca.clinicas.preconsultas.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import py.edu.unca.clinicas.preconsultas.domain.Preconsulta;

/**
 * El paciente se referencia solo por id (sin @ManyToOne) para mantener los slices desacoplados;
 * la integridad la garantiza la FK de la base de datos.
 */
@Entity
@Table(name = "preconsultas")
class PreconsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "presion_sistolica", nullable = false)
    private short presionSistolica;

    @Column(name = "presion_diastolica", nullable = false)
    private short presionDiastolica;

    @Column(name = "frecuencia_cardiaca", nullable = false)
    private short frecuenciaCardiaca;

    @Column(nullable = false, precision = 4, scale = 1)
    private BigDecimal temperatura;

    protected PreconsultaEntity() {
    }

    static PreconsultaEntity desde(Preconsulta p) {
        PreconsultaEntity e = new PreconsultaEntity();
        e.id = p.id();
        e.pacienteId = p.pacienteId();
        e.fechaHora = p.fechaHora();
        e.presionSistolica = (short) p.presionSistolica();
        e.presionDiastolica = (short) p.presionDiastolica();
        e.frecuenciaCardiaca = (short) p.frecuenciaCardiaca();
        e.temperatura = p.temperatura();
        return e;
    }

    Preconsulta aDominio() {
        return new Preconsulta(id, pacienteId, fechaHora, presionSistolica, presionDiastolica,
                frecuenciaCardiaca, temperatura);
    }
}
