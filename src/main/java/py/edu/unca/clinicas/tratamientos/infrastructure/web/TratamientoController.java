package py.edu.unca.clinicas.tratamientos.infrastructure.web;

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

import py.edu.unca.clinicas.shared.web.ErroresApi;
import py.edu.unca.clinicas.shared.web.OpenApiConfig;
import py.edu.unca.clinicas.tratamientos.application.TratamientoService;

@Tag(name = OpenApiConfig.TAG_TRATAMIENTOS)
@RestController
@RequestMapping("/api")
class TratamientoController {

    private final TratamientoService service;

    TratamientoController(TratamientoService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar tratamiento",
            description = "El paciente, el procedimiento y la materia deben existir (404 si no).")
    @ApiResponse(responseCode = "201", description = "Tratamiento creado; la cabecera Location apunta al recurso")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.ReglaDeNegocio
    @PostMapping("/tratamientos")
    ResponseEntity<TratamientoResponse> crear(@Valid @RequestBody TratamientoRequest request) {
        TratamientoResponse creado = TratamientoResponse.desde(service.crear(request.aDominio()));
        return ResponseEntity.created(URI.create("/api/tratamientos/" + creado.id())).body(creado);
    }

    @Operation(summary = "Listar tratamientos", description = "Del más reciente al más antiguo según la fecha de inicio.")
    @ApiResponse(responseCode = "200", description = "Lista de tratamientos")
    @GetMapping("/tratamientos")
    List<TratamientoResponse> listar() {
        return service.listar().stream().map(TratamientoResponse::desde).toList();
    }

    @Operation(summary = "Obtener tratamiento")
    @ApiResponse(responseCode = "200", description = "Tratamiento encontrado")
    @ErroresApi.NoEncontrado
    @GetMapping("/tratamientos/{id}")
    TratamientoResponse obtener(@Parameter(description = "Id del tratamiento") @PathVariable Long id) {
        return TratamientoResponse.desde(service.obtener(id));
    }

    @Operation(summary = "Listar tratamientos de un paciente",
            description = "Del más reciente al más antiguo según la fecha de inicio.")
    @ApiResponse(responseCode = "200", description = "Tratamientos del paciente")
    @ErroresApi.NoEncontrado
    @GetMapping("/pacientes/{pacienteId}/tratamientos")
    List<TratamientoResponse> listarPorPaciente(
            @Parameter(description = "Id del paciente") @PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId).stream().map(TratamientoResponse::desde).toList();
    }

    @Operation(summary = "Actualizar tratamiento", description = "Reemplaza todos los datos del tratamiento.")
    @ApiResponse(responseCode = "200", description = "Tratamiento actualizado")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.ReglaDeNegocio
    @PutMapping("/tratamientos/{id}")
    TratamientoResponse actualizar(@Parameter(description = "Id del tratamiento") @PathVariable Long id,
            @Valid @RequestBody TratamientoRequest request) {
        return TratamientoResponse.desde(service.actualizar(id, request.aDominio()));
    }

    @Operation(summary = "Eliminar tratamiento")
    @ApiResponse(responseCode = "204", description = "Tratamiento eliminado")
    @ErroresApi.NoEncontrado
    @DeleteMapping("/tratamientos/{id}")
    ResponseEntity<Void> eliminar(@Parameter(description = "Id del tratamiento") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
