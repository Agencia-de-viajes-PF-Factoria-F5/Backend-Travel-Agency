package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.ConductorResumenDTO;
import com.inditex.g1_agencia_viajes.model.Conductor;
import org.springframework.stereotype.Component;

@Component
public class ConductorMapper {

    public ConductorResumenDTO toResumenDTO(Conductor conductor) {
        if (conductor == null) return null;
        ConductorResumenDTO dto = new ConductorResumenDTO();
        dto.setId(conductor.getId());
        dto.setNombre(conductor.getNombre());
        dto.setApellidos(conductor.getApellidos());
        dto.setLicencia(conductor.getLicencia());
        return dto;
    }
}