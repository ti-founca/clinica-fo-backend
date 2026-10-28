# Clínicas Backend – Preconsultas

API REST (Spring Boot 4.1, Java 21, Gradle, PostgreSQL, Flyway) para registrar pacientes y sus preconsultas.

## Ejecutar

```bash
cp .env.example .env          # completar credenciales de la base de datos
docker compose up -d          # opcional: levanta PostgreSQL con los datos del .env
./gradlew bootRun
```

Otras tareas: `./gradlew test` (tests), `./gradlew bootJar` (genera `build/libs/clinicas-backend-0.0.1-SNAPSHOT.jar`).

La configuración de la base de datos se lee del archivo `.env` en el directorio desde donde se
ejecuta la aplicación (con `./gradlew bootRun`, la raíz del proyecto; si se ejecuta el `.jar`,
el directorio actual):

| Variable      | Por defecto |
|---------------|-------------|
| `DB_HOST`     | `localhost` |
| `DB_PORT`     | `5432`      |
| `DB_NAME`     | `clinicas`  |
| `DB_USERNAME` | `postgres`  |
| `DB_PASSWORD` | `postgres`  |

Las variables de entorno del sistema tienen prioridad sobre el `.env` (útil en producción/CI).
Si el `.env` no existe se usan los valores por defecto. `.env` está en `.gitignore`; solo se
versiona `.env.example`. Flyway crea el esquema al arrancar (`src/main/resources/db/migration`).

## Documentación (Swagger / OpenAPI)

Con la aplicación en ejecución:

- Swagger UI: <http://localhost:8080/swagger-ui.html>
- Especificación OpenAPI (JSON): <http://localhost:8080/v3/api-docs>

La documentación se genera con [springdoc-openapi](https://springdoc.org) a partir de las
anotaciones de los controladores y DTOs (`@Operation`, `@ApiResponse`, `@Schema`) y de las
validaciones de Bean Validation (rangos, obligatoriedad, longitudes).

## Endpoints

| Método | Ruta                                  | Descripción                         |
|--------|---------------------------------------|-------------------------------------|
| POST   | `/api/pacientes`                      | Crear paciente                      |
| GET    | `/api/pacientes`                      | Listar pacientes                    |
| GET    | `/api/pacientes/{id}`                 | Obtener paciente                    |
| PUT    | `/api/pacientes/{id}`                 | Actualizar paciente                 |
| DELETE | `/api/pacientes/{id}`                 | Eliminar paciente (409 si tiene preconsultas o tratamientos) |
| GET    | `/api/pacientes/{id}/preconsultas`    | Preconsultas del paciente (más reciente primero) |
| POST   | `/api/preconsultas`                   | Crear preconsulta                   |
| GET    | `/api/preconsultas`                   | Listar preconsultas                 |
| GET    | `/api/preconsultas/{id}`              | Obtener preconsulta                 |
| PUT    | `/api/preconsultas/{id}`              | Actualizar preconsulta              |
| DELETE | `/api/preconsultas/{id}`              | Eliminar preconsulta                |
| POST   | `/api/procedimientos`                 | Crear procedimiento                 |
| GET    | `/api/procedimientos`                 | Listar procedimientos (por descripción) |
| GET    | `/api/procedimientos/{id}`            | Obtener procedimiento               |
| PUT    | `/api/procedimientos/{id}`            | Actualizar procedimiento            |
| DELETE | `/api/procedimientos/{id}`            | Eliminar procedimiento (409 si está en uso) |
| POST   | `/api/materias`                       | Crear materia                       |
| GET    | `/api/materias`                       | Listar materias (por descripción)   |
| GET    | `/api/materias/{id}`                  | Obtener materia                     |
| PUT    | `/api/materias/{id}`                  | Actualizar materia                  |
| DELETE | `/api/materias/{id}`                  | Eliminar materia (409 si está en uso) |
| GET    | `/api/pacientes/{id}/tratamientos`    | Tratamientos del paciente (más reciente primero) |
| POST   | `/api/tratamientos`                   | Crear tratamiento                   |
| GET    | `/api/tratamientos`                   | Listar tratamientos                 |
| GET    | `/api/tratamientos/{id}`              | Obtener tratamiento                 |
| PUT    | `/api/tratamientos/{id}`              | Actualizar tratamiento              |
| DELETE | `/api/tratamientos/{id}`              | Eliminar tratamiento                |

Ejemplos de cuerpo:

```json
{ "nombres": "Ana", "apellidos": "Pérez", "numeroDocumento": "1234567",
  "direccion": "Calle 1 c/ Calle 2", "barrio": "Centro", "ciudad": "Asunción" }
```

```json
{ "pacienteId": 1, "fechaHora": "2026-09-30T09:15:00",
  "presionSistolica": 120, "presionDiastolica": 80,
  "frecuenciaCardiaca": 72, "temperatura": 36.5 }
```

```json
{ "descripcion": "Extracción dentaria" }
```

```json
{ "pacienteId": 1, "procedimientoId": 1, "materiaId": 1,
  "fechaInicio": "2026-09-01", "fechaFin": "2026-09-30" }
```

La descripción de procedimientos y materias es única sin distinguir mayúsculas (409 si se repite).
En un tratamiento, `fechaFin` es opcional mientras está en curso y no puede ser anterior a
`fechaInicio` (422).

`fechaHora` es opcional: al crear se usa el momento actual y al actualizar se conserva la registrada.
Los errores se devuelven como `application/problem+json` (400 validación, 404 no encontrado,
409 conflicto, 422 regla de negocio).

## Arquitectura

Clean Architecture organizada por *vertical slices*: cada funcionalidad es un paquete con sus capas.

```
py.edu.unca.clinicas
├── shared/            excepciones de dominio, manejador global de errores y configuración OpenAPI
├── pacientes/
│   ├── domain/        Paciente (record) + PacienteRepository (puerto)
│   ├── application/   PacienteService (casos de uso)
│   └── infrastructure/
│       ├── persistence/  entidad JPA + adaptador del puerto
│       └── web/          controlador REST + DTOs de entrada/salida (documentados con @Schema)
├── preconsultas/      misma estructura
├── procedimientos/    catálogo de procedimientos (misma estructura)
├── materias/          catálogo de materias (misma estructura)
└── tratamientos/      tratamientos de un paciente (procedimiento + materia + fechas)
```

Reglas de dependencia: `domain` no depende de nada; `application` solo de `domain`;
`infrastructure` implementa los puertos y expone la API. Entre slices solo se comparten puertos
de dominio (preconsultas usa `PacienteRepository` para verificar que el paciente exista; tratamientos
usa además `ProcedimientoRepository` y `MateriaRepository`).
