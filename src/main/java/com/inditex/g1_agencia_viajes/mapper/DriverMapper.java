package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.DriverSummaryDTO;
import com.inditex.g1_agencia_viajes.model.Driver;
import org.springframework.stereotype.Component;

@Component
public class DriverMapper {

    public DriverSummaryDTO toSummaryDTO(Driver driver) {
        if (driver == null) return null;
        DriverSummaryDTO dto = new DriverSummaryDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setLastName(driver.getLastName());
        dto.setLicense(driver.getLicense());
        return dto;
    }
}