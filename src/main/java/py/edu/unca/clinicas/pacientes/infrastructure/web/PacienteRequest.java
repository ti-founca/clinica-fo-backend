package py.edu.unca.clinicas.pacientes.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import py.edu.unca.clinicas.pacientes.domain.Paciente;

@Schema(name = "PacienteRequest", description = "Datos para registrar o actualizar un paciente")
record PacienteRequest(
        @Schema(example = "Ana María") @NotBlank @Size(max = 100) String nombres,
        @Schema(example = "Pérez González") @NotBlank @Size(max = 100) String apellidos,
        @Schema(description = "Número de documento de identidad; único por paciente", example = "1234567")
        @NotBlank @Size(max = 20) String numeroDocumento,
        @Schema(example = "Mcal. López 1234 c/ Brasil") @NotBlank @Size(max = 200) String direccion,
        @Schema(example = "Centro") @NotBlank @Size(max = 100) String barrio,
        @Schema(example = "Asunción") @NotBlank @Size(max = 100) String ciudad) {

    Paciente aDominio() {
        return new Paciente(null, nombres.strip(), apellidos.strip(), numeroDocumento.strip(),
                direccion.strip(), barrio.strip(), ciudad.strip());
    }
}
