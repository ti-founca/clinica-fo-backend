package py.edu.unca.clinicas.preconsultas.infrastructure.web;

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

import py.edu.unca.clinicas.preconsultas.application.PreconsultaService;
import py.edu.unca.clinicas.shared.web.ErroresApi;
import py.edu.unca.clinicas.shared.web.OpenApiConfig;

@Tag(name = OpenApiConfig.TAG_PRECONSULTAS)
@RestController
@RequestMapping("/api")
class PreconsultaController {

    private final PreconsultaService service;

    PreconsultaController(PreconsultaService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar preconsulta", description = "Si no se envía `fechaHora` se usa el momento actual.")
    @ApiResponse(responseCode = "201", description = "Preconsulta creada; la cabecera Location apunta al recurso")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.ReglaDeNegocio
    @PostMapping("/preconsultas")
    ResponseEntity<PreconsultaResponse> crear(@Valid @RequestBody PreconsultaRequest request) {
        PreconsultaResponse creada = PreconsultaResponse.desde(service.crear(request.aDominio()));
        return ResponseEntity.created(URI.create("/api/preconsultas/" + creada.id())).body(creada);
    }

    @Operation(summary = "Listar preconsultas", description = "De la más reciente a la más antigua.")
    @ApiResponse(responseCode = "200", description = "Lista de preconsultas")
    @GetMapping("/preconsultas")
    List<PreconsultaResponse> listar() {
        return service.listar().stream().map(PreconsultaResponse::desde).toList();
    }

    @Operation(summary = "Obtener preconsulta")
    @ApiResponse(responseCode = "200", description = "Preconsulta encontrada")
    @ErroresApi.NoEncontrado
    @GetMapping("/preconsultas/{id}")
    PreconsultaResponse obtener(@Parameter(description = "Id de la preconsulta") @PathVariable Long id) {
        return PreconsultaResponse.desde(service.obtener(id));
    }

    @Operation(summary = "Listar preconsultas de un paciente", description = "De la más reciente a la más antigua.")
    @ApiResponse(responseCode = "200", description = "Preconsultas del paciente")
    @ErroresApi.NoEncontrado
    @GetMapping("/pacientes/{pacienteId}/preconsultas")
    List<PreconsultaResponse> listarPorPaciente(
            @Parameter(description = "Id del paciente") @PathVariable Long pacienteId) {
        return service.listarPorPaciente(pacienteId).stream().map(PreconsultaResponse::desde).toList();
    }

    @Operation(summary = "Actualizar preconsulta",
            description = "Reemplaza todos los datos. Si no se envía `fechaHora` se conserva la registrada.")
    @ApiResponse(responseCode = "200", description = "Preconsulta actualizada")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.ReglaDeNegocio
    @PutMapping("/preconsultas/{id}")
    PreconsultaResponse actualizar(@Parameter(description = "Id de la preconsulta") @PathVariable Long id,
            @Valid @RequestBody PreconsultaRequest request) {
        return PreconsultaResponse.desde(service.actualizar(id, request.aDominio()));
    }

    @Operation(summary = "Eliminar preconsulta")
    @ApiResponse(responseCode = "204", description = "Preconsulta eliminada")
    @ErroresApi.NoEncontrado
    @DeleteMapping("/preconsultas/{id}")
    ResponseEntity<Void> eliminar(@Parameter(description = "Id de la preconsulta") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
