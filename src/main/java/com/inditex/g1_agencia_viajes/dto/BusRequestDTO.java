package com.inditex.g1_agencia_viajes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BusRequestDTO {

    @NotBlank(message = "La matrícula es obligatoria")
    private String licensePlate;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor que 0")
    private Integer capacity;

    private Boolean bath;
    private Boolean wifi;
    private Boolean AC;
    private Boolean USB;
    private Boolean available;

}