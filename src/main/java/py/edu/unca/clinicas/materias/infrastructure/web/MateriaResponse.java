package py.edu.unca.clinicas.materias.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.unca.clinicas.materias.domain.Materia;

@Schema(name = "Materia", description = "Materia registrada")
record MateriaResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Clínica de Operatoria Dental I") String descripcion) {

    static MateriaResponse desde(Materia m) {
        return new MateriaResponse(m.id(), m.descripcion());
    }
}
