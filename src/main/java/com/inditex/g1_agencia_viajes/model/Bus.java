package com.inditex.g1_agencia_viajes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "buses")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private Integer capacity;

    /* @Column(nullable = false)
    private Boolean available = true; */

    private Boolean bath;
    private Boolean wifi;
    private Boolean AC;
    private Boolean USB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;
}