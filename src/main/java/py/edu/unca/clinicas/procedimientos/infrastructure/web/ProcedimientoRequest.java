package py.edu.unca.clinicas.procedimientos.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;

@Schema(name = "ProcedimientoRequest", description = "Datos para registrar o actualizar un procedimiento")
record ProcedimientoRequest(
        @Schema(description = "Única, sin distinguir mayúsculas", example = "Extracción dentaria")
        @NotBlank @Size(max = 150) String descripcion) {

    Procedimiento aDominio() {
        return new Procedimiento(null, descripcion.strip());
    }
}
