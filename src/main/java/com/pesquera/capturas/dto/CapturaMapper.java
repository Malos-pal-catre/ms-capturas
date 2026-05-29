package com.pesquera.capturas.dto;

import com.pesquera.capturas.model.Captura;

public class CapturaMapper {

    public static CapturaResponseDTO toDTO(Captura c) {
        return new CapturaResponseDTO(
            c.getId(),
            c.getPescadorId(),
            c.getEmbarcacionId(),
            c.getEspecieId(),
            c.getNombreEspecie(),
            c.getKgTotal(),
            c.getFecha(),
            c.getEstado()
        );
    }

    public static Captura toEntity(CapturaRequestDTO dto, String nombreEspecie) {
        return Captura.builder()
            .pescadorId(dto.pescadorId())
            .embarcacionId(dto.embarcacionId())
            .especieId(dto.especieId())
            .nombreEspecie(nombreEspecie)
            .kgTotal(dto.kgTotal())
            .fecha(dto.fecha())
            .estado("PENDIENTE")
            .build();
    }
}