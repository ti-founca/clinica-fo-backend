package py.edu.unca.clinicas.procedimientos.infrastructure.web;

import io.swagger.v3.oas.annotations.media.Schema;

import py.edu.unca.clinicas.procedimientos.domain.Procedimiento;

@Schema(name = "Procedimiento", description = "Procedimiento registrado")
record ProcedimientoResponse(
        @Schema(example = "1") Long id,
        @Schema(example = "Extracción dentaria") String descripcion) {

    static ProcedimientoResponse desde(Procedimiento m) {
        return new ProcedimientoResponse(m.id(), m.descripcion());
    }
}
