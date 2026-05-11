package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

@Data
public class DriverResponseDTO {
    private Long id;
    private String name;
    private String phone;
    private String enrollment;
    private Boolean licenceActive;
    private String imageUrl;
}