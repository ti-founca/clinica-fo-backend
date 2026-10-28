CREATE TABLE tratamientos (
    id               BIGSERIAL PRIMARY KEY,
    paciente_id      BIGINT    NOT NULL,
    procedimiento_id BIGINT    NOT NULL,
    materia_id       BIGINT    NOT NULL,
    fecha_inicio     DATE      NOT NULL,
    -- Nula mientras el tratamiento está en curso.
    fecha_fin        DATE,
    -- RESTRICT: no se pueden eliminar pacientes, procedimientos ni materias con tratamientos registrados.
    CONSTRAINT fk_tratamientos_paciente FOREIGN KEY (paciente_id)
        REFERENCES pacientes (id) ON DELETE RESTRICT,
    CONSTRAINT fk_tratamientos_procedimiento FOREIGN KEY (procedimiento_id)
        REFERENCES procedimientos (id) ON DELETE RESTRICT,
    CONSTRAINT fk_tratamientos_materia FOREIGN KEY (materia_id)
        REFERENCES materias (id) ON DELETE RESTRICT,
    CONSTRAINT ck_tratamientos_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

CREATE INDEX idx_tratamientos_paciente_fecha ON tratamientos (paciente_id, fecha_inicio DESC);
CREATE INDEX idx_tratamientos_procedimiento ON tratamientos (procedimiento_id);
CREATE INDEX idx_tratamientos_materia ON tratamientos (materia_id);
