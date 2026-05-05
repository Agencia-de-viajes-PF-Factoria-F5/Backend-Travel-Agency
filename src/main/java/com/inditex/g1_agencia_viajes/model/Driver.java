package com.inditex.g1_agencia_viajes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false, unique = true)
    private String enrollment;

    @Column(nullable = false)
    private Boolean licenceActive = true;

    private String imageUrl;
}