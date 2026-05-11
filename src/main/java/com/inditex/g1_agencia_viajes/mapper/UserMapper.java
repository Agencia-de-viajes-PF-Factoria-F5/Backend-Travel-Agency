package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.UserRequestDTO;
import com.inditex.g1_agencia_viajes.dto.UserResponseDTO;
import com.inditex.g1_agencia_viajes.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setDni(dto.getDni());
        user.setPassport(dto.getPassport());
        user.setAge(dto.getAge());
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }
        return user;
    }

    public UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setDni(user.getDni());
        dto.setPassport(user.getPassport());
        dto.setAge(user.getAge());
        dto.setActive(user.getActive());
        if (user.getTutorId() != null) {
            dto.setTutorId(user.getTutorId().getId());
        }
        return dto;
    }
}