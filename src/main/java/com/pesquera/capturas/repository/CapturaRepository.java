package com.pesquera.capturas.repository;

import com.pesquera.capturas.model.Captura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CapturaRepository extends JpaRepository<Captura, Long> {

    // Query Methods
    List<Captura> findByPescadorId(Long pescadorId);
    List<Captura> findByEspecieId(Long especieId);
    List<Captura> findByEstado(String estado);
    List<Captura> findByFecha(LocalDate fecha);

    // Custom Queries
    @Query("SELECT c FROM Captura c WHERE c.pescadorId = :pescadorId AND c.fecha = :fecha")
    List<Captura> buscarPorPescadorYFecha(@Param("pescadorId") Long pescadorId,
                                          @Param("fecha") LocalDate fecha);

    @Query("SELECT SUM(c.kgTotal) FROM Captura c WHERE c.especieId = :especieId AND c.fecha = :fecha")
    Double totalKgPorEspecieYFecha(@Param("especieId") Long especieId,
                                   @Param("fecha") LocalDate fecha);

    @Query(value = "SELECT * FROM capturas WHERE estado = 'PENDIENTE' ORDER BY fecha ASC", nativeQuery = true)
    List<Captura> capturasEsperandoSubasta();
}