CREATE TABLE pacientes (
    id               BIGSERIAL    PRIMARY KEY,
    nombres          VARCHAR(100) NOT NULL,
    apellidos        VARCHAR(100) NOT NULL,
    numero_documento VARCHAR(20)  NOT NULL,
    direccion        VARCHAR(200) NOT NULL,
    barrio           VARCHAR(100) NOT NULL,
    ciudad           VARCHAR(100) NOT NULL,
    CONSTRAINT uk_pacientes_numero_documento UNIQUE (numero_documento)
);
