package com.inditex.g1_agencia_viajes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {
    @NotNull(message = "El ID del empleado es obligatorio")
    private Long id;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
