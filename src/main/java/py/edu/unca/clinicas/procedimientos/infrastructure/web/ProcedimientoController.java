package py.edu.unca.clinicas.procedimientos.infrastructure.web;

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

import py.edu.unca.clinicas.procedimientos.application.ProcedimientoService;
import py.edu.unca.clinicas.shared.web.ErroresApi;
import py.edu.unca.clinicas.shared.web.OpenApiConfig;

@Tag(name = OpenApiConfig.TAG_PROCEDIMIENTOS)
@RestController
@RequestMapping("/api/procedimientos")
class ProcedimientoController {

    private final ProcedimientoService service;

    ProcedimientoController(ProcedimientoService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar procedimiento")
    @ApiResponse(responseCode = "201", description = "Procedimiento creado; la cabecera Location apunta al recurso")
    @ErroresApi.DatosInvalidos
    @ErroresApi.Conflicto
    @PostMapping
    ResponseEntity<ProcedimientoResponse> crear(@Valid @RequestBody ProcedimientoRequest request) {
        ProcedimientoResponse creado = ProcedimientoResponse.desde(service.crear(request.aDominio()));
        return ResponseEntity.created(URI.create("/api/procedimientos/" + creado.id())).body(creado);
    }

    @Operation(summary = "Listar procedimientos", description = "Ordenados por descripción.")
    @ApiResponse(responseCode = "200", description = "Lista de procedimientos")
    @GetMapping
    List<ProcedimientoResponse> listar() {
        return service.listar().stream().map(ProcedimientoResponse::desde).toList();
    }

    @Operation(summary = "Obtener procedimiento")
    @ApiResponse(responseCode = "200", description = "Procedimiento encontrado")
    @ErroresApi.NoEncontrado
    @GetMapping("/{id}")
    ProcedimientoResponse obtener(@Parameter(description = "Id del procedimiento") @PathVariable Long id) {
        return ProcedimientoResponse.desde(service.obtener(id));
    }

    @Operation(summary = "Actualizar procedimiento")
    @ApiResponse(responseCode = "200", description = "Procedimiento actualizado")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @PutMapping("/{id}")
    ProcedimientoResponse actualizar(@Parameter(description = "Id del procedimiento") @PathVariable Long id,
            @Valid @RequestBody ProcedimientoRequest request) {
        return ProcedimientoResponse.desde(service.actualizar(id, request.aDominio()));
    }

    @Operation(summary = "Eliminar procedimiento",
            description = "No se puede eliminar si está asignado a algún tratamiento (409).")
    @ApiResponse(responseCode = "204", description = "Procedimiento eliminado")
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@Parameter(description = "Id del procedimiento") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
