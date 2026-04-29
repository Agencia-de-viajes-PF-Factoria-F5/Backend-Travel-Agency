package com.inditex.g1_agencia_viajes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BusDTO {

    private Long id;

    @NotBlank(message = "La matrícula es obligatoria")
    private String licensePlate;

    @NotBlank(message = "La marca es obligatoria")
    private String brand;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor que 0")
    private Integer capacity;

    private Integer year;

    private Boolean available = true;
}