package com.pesquera.capturas.service;

import com.pesquera.capturas.client.EspecieClientService;
import com.pesquera.capturas.dto.*;
import com.pesquera.capturas.exception.RecursoNoEncontradoException;
import com.pesquera.capturas.model.Captura;
import com.pesquera.capturas.repository.CapturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CapturaService {

    private final CapturaRepository capturaRepository;
    private final EspecieClientService especieClientService;

    public List<CapturaResponseDTO> listarTodas() {
        return capturaRepository.findAll()
            .stream()
            .map(CapturaMapper::toDTO)
            .toList();
    }

    public CapturaResponseDTO buscarPorId(Long id) {
        Captura c = capturaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Captura no encontrada con id: " + id));
        return CapturaMapper.toDTO(c);
    }

    public List<CapturaResponseDTO> listarPorPescador(Long pescadorId) {
        return capturaRepository.findByPescadorId(pescadorId)
            .stream()
            .map(CapturaMapper::toDTO)
            .toList();
    }

    public List<CapturaResponseDTO> listarPendientes() {
        return capturaRepository.capturasEsperandoSubasta()
            .stream()
            .map(CapturaMapper::toDTO)
            .toList();
    }

    public List<CapturaResponseDTO> listarPorFecha(LocalDate fecha) {
        return capturaRepository.findByFecha(fecha)
            .stream()
            .map(CapturaMapper::toDTO)
            .toList();
    }

    public CapturaResponseDTO registrar(CapturaRequestDTO dto) {
        // 1. Obtener especie desde ms-especies via WebClient
        EspecieResponseDTO especie = especieClientService.obtenerEspecie(dto.especieId());

        // 2. Validar que no tenga veda activa
        if (especie.vedaActiva()) {
            throw new IllegalArgumentException("La especie " + especie.nombre() + " tiene veda activa");
        }

        // 3. Validar que haya cuota suficiente
        if (especie.cuotaDisponibleKg() < dto.kgTotal()) {
            throw new IllegalArgumentException("Cuota insuficiente para " + especie.nombre() +
                ". Disponible: " + especie.cuotaDisponibleKg() + " kg");
        }

        // 4. Descontar cuota en ms-especies
        especieClientService.descontarCuota(dto.especieId(), dto.kgTotal());

        // 5. Guardar captura
        Captura nueva = CapturaMapper.toEntity(dto, especie.nombre());
        return CapturaMapper.toDTO(capturaRepository.save(nueva));
    }

    public CapturaResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Captura c = capturaRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Captura no encontrada con id: " + id));
        c.setEstado(nuevoEstado);
        return CapturaMapper.toDTO(capturaRepository.save(c));
    }

    public void eliminar(Long id) {
        if (!capturaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Captura no encontrada con id: " + id);
        }
        capturaRepository.deleteById(id);
    }
}