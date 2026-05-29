package com.pesquera.capturas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "capturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Captura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pescadorId;

    @Column(nullable = false)
    private Long embarcacionId;

    @Column(nullable = false)
    private Long especieId;

    @Column(nullable = false)
    private String nombreEspecie;

    @Column(nullable = false)
    private Double kgTotal;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String estado; // PENDIENTE, EN_SUBASTA, VENDIDO
}