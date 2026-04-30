package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

@Data
public class ConductorResumenDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String licencia;
}