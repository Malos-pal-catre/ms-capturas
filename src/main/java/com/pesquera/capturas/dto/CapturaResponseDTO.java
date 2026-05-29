package com.pesquera.capturas.dto;

import java.time.LocalDate;

public record CapturaResponseDTO(
    Long id,
    Long pescadorId,
    Long embarcacionId,
    Long especieId,
    String nombreEspecie,
    Double kgTotal,
    LocalDate fecha,
    String estado
) {}