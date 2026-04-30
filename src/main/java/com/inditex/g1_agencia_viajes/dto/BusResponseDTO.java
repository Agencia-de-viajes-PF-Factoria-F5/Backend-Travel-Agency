package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

@Data
public class BusResponseDTO {

    private Long id;
    private String licensePlate;
    private String brand;
    private Integer capacity;
    private Integer year;
    private Boolean available;
    private DriverSummaryDTO driver;
}