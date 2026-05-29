package com.pesquera.capturas.client;

import com.pesquera.capturas.dto.EspecieResponseDTO;
import com.pesquera.capturas.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EspecieClientService {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.ms-especies.url}")
    private String especiesUrl;

    public EspecieResponseDTO obtenerEspecie(Long id) {
        return webClientBuilder.baseUrl(especiesUrl).build()
            .get()
            .uri("/api/especies/{id}", id)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(),
                resp -> Mono.error(new RecursoNoEncontradoException("Especie no encontrada con id: " + id)))
            .bodyToMono(EspecieResponseDTO.class)
            .block();
    }

    public Boolean tieneVedaActiva(String nombre) {
        return webClientBuilder.baseUrl(especiesUrl).build()
            .get()
            .uri("/api/especies/veda?nombre={nombre}", nombre)
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
    }

    public EspecieResponseDTO descontarCuota(Long id, Double kg) {
        return webClientBuilder.baseUrl(especiesUrl).build()
            .patch()
            .uri("/api/especies/{id}/descontar-cuota?kg={kg}", id, kg)
            .retrieve()
            .onStatus(status -> status.is4xxClientError(),
                resp -> Mono.error(new IllegalArgumentException("No se pudo descontar cuota para especie id: " + id)))
            .bodyToMono(EspecieResponseDTO.class)
            .block();
    }
}