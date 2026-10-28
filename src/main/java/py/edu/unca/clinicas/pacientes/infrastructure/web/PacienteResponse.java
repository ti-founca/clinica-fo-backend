package py.edu.unca.clinicas.pacientes.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.unca.clinicas.pacientes.domain.Paciente;

@Schema(name = "Paciente", description = "Paciente registrado")
record PacienteResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Ana María") String nombres,
        @Schema(example = "Pérez González") String apellidos,
        @Schema(example = "1234567") String numeroDocumento,
        @Schema(example = "Mcal. López 1234 c/ Brasil") String direccion,
        @Schema(example = "Centro") String barrio,
        @Schema(example = "Asunción") String ciudad) {

    static PacienteResponse desde(Paciente p) {
        return new PacienteResponse(p.id(), p.nombres(), p.apellidos(), p.numeroDocumento(),
                p.direccion(), p.barrio(), p.ciudad());
    }
}
