package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.DriverRequestDTO;
import com.inditex.g1_agencia_viajes.dto.DriverResponseDTO;
import com.inditex.g1_agencia_viajes.model.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public Driver toEntity(DriverRequestDTO dto) {
        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setPhone(dto.getPhone());
        driver.setImageUrl(dto.getImageUrl());
        if (dto.getLicenceActive() != null) {
            driver.setLicenceActive(dto.getLicenceActive());
        }
        if (dto.getActive() != null) {
            driver.setActive(dto.getActive());
        }
        return driver;
    }

    public DriverResponseDTO toDTO(Driver driver) {
        DriverResponseDTO dto = new DriverResponseDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setLicenceActive(driver.getLicenceActive());
        dto.setImageUrl(driver.getImageUrl());
        dto.setActive(driver.getActive());
        return dto;
    }

}
