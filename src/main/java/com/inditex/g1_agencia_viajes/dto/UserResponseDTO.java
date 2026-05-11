package com.inditex.g1_agencia_viajes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter
@Setter
@Data
public class UserResponseDTO {
    private Long userId;
    private String fullName;

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String dni;

    public UserResponseDTO() {}
    private String passport;
    private Integer age;
    private Long tutorId;
    private Boolean active;
}