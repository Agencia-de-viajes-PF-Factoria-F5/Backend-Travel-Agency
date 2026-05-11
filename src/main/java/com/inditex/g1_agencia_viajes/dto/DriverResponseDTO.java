package com.inditex.g1_agencia_viajes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DriverRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String phone;

    @NotBlank(message = "La licencia es obligatoria")
    private String enrollment;

    private Boolean licenceActive;

    private String imageUrl;
}