package com.inditex.g1_agencia_viajes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotBlank(message = "La licencia es obligatoria")
    private String enrollment;

    private Boolean licenceActive = true;

    private String imageUrl;
}