package com.inditex.g1_agencia_viajes.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String dni;

    public UserResponseDTO() {}
}
