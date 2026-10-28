package py.edu.unca.clinicas.materias.infrastructure.web;

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

import py.edu.unca.clinicas.materias.application.MateriaService;
import py.edu.unca.clinicas.shared.web.ErroresApi;
import py.edu.unca.clinicas.shared.web.OpenApiConfig;

@Tag(name = OpenApiConfig.TAG_MATERIAS)
@RestController
@RequestMapping("/api/materias")
class MateriaController {

    private final MateriaService service;

    MateriaController(MateriaService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar materia")
    @ApiResponse(responseCode = "201", description = "Materia creada; la cabecera Location apunta al recurso")
    @ErroresApi.DatosInvalidos
    @ErroresApi.Conflicto
    @PostMapping
    ResponseEntity<MateriaResponse> crear(@Valid @RequestBody MateriaRequest request) {
        MateriaResponse creada = MateriaResponse.desde(service.crear(request.aDominio()));
        return ResponseEntity.created(URI.create("/api/materias/" + creada.id())).body(creada);
    }

    @Operation(summary = "Listar materias", description = "Ordenadas por descripción.")
    @ApiResponse(responseCode = "200", description = "Lista de materias")
    @GetMapping
    List<MateriaResponse> listar() {
        return service.listar().stream().map(MateriaResponse::desde).toList();
    }

    @Operation(summary = "Obtener materia")
    @ApiResponse(responseCode = "200", description = "Materia encontrada")
    @ErroresApi.NoEncontrado
    @GetMapping("/{id}")
    MateriaResponse obtener(@Parameter(description = "Id de la materia") @PathVariable Long id) {
        return MateriaResponse.desde(service.obtener(id));
    }

    @Operation(summary = "Actualizar materia")
    @ApiResponse(responseCode = "200", description = "Materia actualizada")
    @ErroresApi.DatosInvalidos
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @PutMapping("/{id}")
    MateriaResponse actualizar(@Parameter(description = "Id de la materia") @PathVariable Long id,
            @Valid @RequestBody MateriaRequest request) {
        return MateriaResponse.desde(service.actualizar(id, request.aDominio()));
    }

    @Operation(summary = "Eliminar materia",
            description = "No se puede eliminar si está asignada a algún tratamiento (409).")
    @ApiResponse(responseCode = "204", description = "Materia eliminada")
    @ErroresApi.NoEncontrado
    @ErroresApi.Conflicto
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@Parameter(description = "Id de la materia") @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
