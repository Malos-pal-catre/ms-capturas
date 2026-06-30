package com.pesquera.capturas.controller;

import com.pesquera.capturas.dto.*;
import com.pesquera.capturas.service.CapturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/capturas")
@RequiredArgsConstructor
@Tag(name = "Capturas", description = "Registro y gestión del ciclo de vida de las capturas pesqueras (pendiente, validada, rechazada)")
public class CapturaController {

    private final CapturaService capturaService;

    @Operation(summary = "Listar todas las capturas", description = "Retorna el listado completo de capturas registradas en el sistema, sin aplicar filtros.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<CapturaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(capturaService.listarTodas());
    }

    @Operation(summary = "Buscar captura por ID", description = "Retorna el detalle de una captura específica.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Captura encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe una captura con el ID indicado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CapturaResponseDTO> buscarPorId(
            @Parameter(description = "ID de la captura", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(capturaService.buscarPorId(id));
    }

    @Operation(summary = "Listar capturas por pescador", description = "Retorna todas las capturas asociadas a un pescador específico.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/pescador/{pescadorId}")
    public ResponseEntity<List<CapturaResponseDTO>> listarPorPescador(
            @Parameter(description = "ID del pescador", example = "3") @PathVariable Long pescadorId) {
        return ResponseEntity.ok(capturaService.listarPorPescador(pescadorId));
    }

    @Operation(summary = "Listar capturas pendientes", description = "Retorna las capturas que aún no han sido validadas ni rechazadas, candidatas a entrar a subasta.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/pendientes")
    public ResponseEntity<List<CapturaResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(capturaService.listarPendientes());
    }

    @Operation(summary = "Listar capturas por fecha", description = "Filtra las capturas registradas en una fecha específica.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/fecha")
    public ResponseEntity<List<CapturaResponseDTO>> listarPorFecha(
            @Parameter(description = "Fecha de la captura (yyyy-MM-dd)", example = "2026-03-15") @RequestParam LocalDate fecha) {
        return ResponseEntity.ok(capturaService.listarPorFecha(fecha));
    }

    @Operation(
        summary = "Registrar una nueva captura",
        description = "Crea una captura nueva. El servicio valida contra ms-especies que la especie no tenga una veda activa y que exista cuota disponible antes de registrar."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            schema = @Schema(implementation = CapturaRequestDTO.class),
            examples = @ExampleObject(
                name = "Captura de ejemplo",
                value = """
                {
                  "pescadorId": 3,
                  "especieId": 2,
                  "embarcacionId": 1,
                  "kilos": 150.5,
                  "fecha": "2026-03-15"
                }
                """
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Captura registrada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o especie con veda activa", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CapturaResponseDTO> registrar(@RequestBody @Valid CapturaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(capturaService.registrar(dto));
    }

    @Operation(summary = "Cambiar estado de una captura", description = "Actualiza el estado de una captura (ej: PENDIENTE → VALIDADA o RECHAZADA).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Captura no encontrada", content = @Content)
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CapturaResponseDTO> cambiarEstado(
            @Parameter(description = "ID de la captura", example = "1") @PathVariable Long id,
            @Parameter(description = "Nuevo estado", example = "VALIDADA") @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(capturaService.cambiarEstado(id, nuevoEstado));
    }

    @Operation(summary = "Eliminar una captura", description = "Elimina de forma permanente una captura del sistema.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Captura eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Captura no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID de la captura", example = "1") @PathVariable Long id) {
        capturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}