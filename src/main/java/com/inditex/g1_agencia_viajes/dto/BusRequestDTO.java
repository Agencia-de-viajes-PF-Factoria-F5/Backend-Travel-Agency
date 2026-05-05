package com.inditex.g1_agencia_viajes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusRequestDTO {

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be greater than 0")
    private Integer capacity;

    private Integer year;

    private Long driverId;
}