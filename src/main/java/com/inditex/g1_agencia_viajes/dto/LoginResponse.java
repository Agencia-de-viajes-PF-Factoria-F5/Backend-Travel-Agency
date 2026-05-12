package com.inditex.g1_agencia_viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private Long employeeId;
    private String name;
    private String surname;
}
