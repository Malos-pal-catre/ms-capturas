package com.pesquera.capturas.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CapturaRequestDTO(

    @NotNull(message = "El ID del pescador es obligatorio")
    @Positive(message = "El ID del pescador debe ser mayor a 0")
    Long pescadorId,

    @NotNull(message = "El ID de la embarcación es obligatorio")
    @Positive(message = "El ID de la embarcación debe ser mayor a 0")
    Long embarcacionId,

    @NotNull(message = "El ID de la especie es obligatorio")
    @Positive(message = "El ID de la especie debe ser mayor a 0")
    Long especieId,

    @NotNull(message = "El total en kg es obligatorio")
    @Positive(message = "El total en kg debe ser mayor a 0")
    Double kgTotal,

    @NotNull(message = "La fecha es obligatoria")
    LocalDate fecha
) {}