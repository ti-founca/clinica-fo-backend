CREATE TABLE procedimientos (
    id          BIGSERIAL    PRIMARY KEY,
    descripcion VARCHAR(150) NOT NULL
);

-- La descripción es única sin distinguir mayúsculas.
CREATE UNIQUE INDEX uk_procedimientos_descripcion ON procedimientos (LOWER(descripcion));

CREATE TABLE materias (
    id          BIGSERIAL    PRIMARY KEY,
    descripcion VARCHAR(150) NOT NULL
);

CREATE UNIQUE INDEX uk_materias_descripcion ON materias (LOWER(descripcion));
