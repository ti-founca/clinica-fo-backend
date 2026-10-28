package py.edu.unca.clinicas.pacientes.infrastructure.web;

import java.net.URI;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import py.edu.unca.clinicas.pacientes.application.PacienteService;
import py.edu.unca.clinicas.shared.web.ErroresApi;
import py.edu.unca.clinicas.shared.web.OpenApiConfig;

@Tag(name = OpenApiConfig.TAG_PACIENTES)
@RestController
@RequestMapping("/api/pacientes")
class PacienteController {

    private final PacienteService service;

    PacienteController(PacienteService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar paciente")
    @ApiResponse(responseCode = "201", description = "Paciente creado; la cabecera Location apunta al recurso")
    @ErroresApi.DatosInvalidos
    @ErroresApi.Conflicto
    @PostMapping
    ResponseEntity<PacienteResponse> crear(@Valid @RequestBody PacienteRequest request) {
        PacienteResponse creado = PacienteResponse.desde(service.crear(request.aDominio()));
        return ResponseEntity.created(URI.create("/api/pacientes/" + creado.id())).body(creado);
    }

    @Operation(summary = "Listar pacientes", description = "Ordenados por apellidos y nombres.")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes")
    @GetMapping
    List<PacienteResponse> listar() {
        return service.listar().stream().map(PacienteResponse::desde).toList();
    }

    @Operation(summary = "Obtener paciente")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado")
    @ErroresApi.NoEncontrado
    @GetMapping("/{id}")
    PacienteResponse obtener(@Parameter(description = "Id del paciente") @PathVariable Long id) {
        return PacienteResponse.desde(service.obtener(id));
    }

    @Operation(summary = "Actualizar paciente", description = "Reemplaza todos los datos del paciente.")
    @ApiResponse(responseCode = "200", description = "Paciente actualizado")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @PutMapping("/{id}")
    PacienteResponse actualizar(@Parameter(description = "Id del paciente") @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request) {
        return PacienteResponse.desde(service.actualizar(id, request.aDominio()));
    }

    @Operation(summary = "Eliminar paciente",
            description = "No se puede eliminar un paciente que tenga preconsultas o tratamientos registrados (409).")
    @ApiResponse(responseCode = "204", description = "Paciente eliminado")
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@Parameter(description = "Id del paciente") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
