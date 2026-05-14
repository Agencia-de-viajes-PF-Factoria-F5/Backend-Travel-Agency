package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.TripSegmentRequestDTO;
import com.inditex.g1_agencia_viajes.dto.TripSegmentResponseDTO;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.model.Driver;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.model.TripSegment;
import org.springframework.stereotype.Component;

@Component
public class TripSegmentMapper {

    public TripSegment toEntity(TripSegmentRequestDTO dto, Travel travel, Bus bus, Driver driver) {
        TripSegment segment = new TripSegment();
        segment.setTravel(travel);
        segment.setOrigin(dto.getOrigin());
        segment.setDestination(dto.getDestination());
        segment.setStartTime(dto.getStartTime());
        segment.setEndTime(dto.getEndTime());
        segment.setBus(bus);
        segment.setDriver(driver);
        return segment;
    }

    public TripSegmentResponseDTO toDTO(TripSegment segment) {
        TripSegmentResponseDTO dto = new TripSegmentResponseDTO();
        dto.setId(segment.getId());
        dto.setTravelId(segment.getTravel().getId());
        dto.setOrigin(segment.getOrigin());
        dto.setDestination(segment.getDestination());
        dto.setStartTime(segment.getStartTime());
        dto.setEndTime(segment.getEndTime());
        dto.setBusId(segment.getBus().getId());
        dto.setBusLicensePlate(segment.getBus().getLicensePlate());
        dto.setDriverId(segment.getDriver().getId());
        dto.setDriverName(segment.getDriver().getName());
        return dto;
    }
}
