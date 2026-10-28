CREATE TABLE preconsultas (
    id                  BIGSERIAL    PRIMARY KEY,
    paciente_id         BIGINT       NOT NULL,
    fecha_hora          TIMESTAMP    NOT NULL,
    presion_sistolica   SMALLINT     NOT NULL,
    presion_diastolica  SMALLINT     NOT NULL,
    frecuencia_cardiaca SMALLINT     NOT NULL,
    temperatura         NUMERIC(4,1) NOT NULL,
    -- RESTRICT: no se puede eliminar un paciente que tenga preconsultas registradas.
    CONSTRAINT fk_preconsultas_paciente FOREIGN KEY (paciente_id)
        REFERENCES pacientes (id) ON DELETE RESTRICT,
    CONSTRAINT ck_preconsultas_presion CHECK (presion_sistolica > presion_diastolica)
);

CREATE INDEX idx_preconsultas_paciente_fecha ON preconsultas (paciente_id, fecha_hora DESC);
