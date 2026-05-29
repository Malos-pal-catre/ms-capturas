package com.pesquera.capturas.controller;

import com.pesquera.capturas.dto.*;
import com.pesquera.capturas.service.CapturaService;
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
public class CapturaController {

    private final CapturaService capturaService;

    @GetMapping
    public ResponseEntity<List<CapturaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(capturaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CapturaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(capturaService.buscarPorId(id));
    }

    @GetMapping("/pescador/{pescadorId}")
    public ResponseEntity<List<CapturaResponseDTO>> listarPorPescador(@PathVariable Long pescadorId) {
        return ResponseEntity.ok(capturaService.listarPorPescador(pescadorId));
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<CapturaResponseDTO>> listarPendientes() {
        return ResponseEntity.ok(capturaService.listarPendientes());
    }

    @GetMapping("/fecha")
    public ResponseEntity<List<CapturaResponseDTO>> listarPorFecha(@RequestParam LocalDate fecha) {
        return ResponseEntity.ok(capturaService.listarPorFecha(fecha));
    }

    @PostMapping
    public ResponseEntity<CapturaResponseDTO> registrar(@RequestBody @Valid CapturaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(capturaService.registrar(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<CapturaResponseDTO> cambiarEstado(@PathVariable Long id,
                                                             @RequestParam String nuevoEstado) {
        return ResponseEntity.ok(capturaService.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        capturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}