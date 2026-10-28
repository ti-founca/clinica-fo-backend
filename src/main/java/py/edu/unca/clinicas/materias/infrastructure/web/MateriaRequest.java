package py.edu.unca.clinicas.materias.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import py.edu.unca.clinicas.materias.domain.Materia;

@Schema(name = "MateriaRequest", description = "Datos para registrar o actualizar una materia")
record MateriaRequest(
        @Schema(description = "Única, sin distinguir mayúsculas", example = "Clínica de Operatoria Dental I")
        @NotBlank @Size(max = 150) String descripcion) {

    Materia aDominio() {
        return new Materia(null, descripcion.strip());
    }
}
