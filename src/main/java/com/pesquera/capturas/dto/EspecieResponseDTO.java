package com.pesquera.capturas.dto;

// DTO espejo del ms-especies, solo los campos que necesitamos
public record EspecieResponseDTO(
    Long id,
    String nombre,
    Double cuotaAnualKg,
    Double cuotaDisponibleKg,
    Boolean vedaActiva,
    String zona
) {}