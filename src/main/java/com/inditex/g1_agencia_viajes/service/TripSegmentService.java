package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.TripSegmentRequestDTO;
import com.inditex.g1_agencia_viajes.dto.TripSegmentResponseDTO;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.mapper.TripSegmentMapper;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.model.Driver;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.model.TripSegment;
import com.inditex.g1_agencia_viajes.repository.BusRepository;
import com.inditex.g1_agencia_viajes.repository.DriverRepository;
import com.inditex.g1_agencia_viajes.repository.TravelRepository;
import com.inditex.g1_agencia_viajes.repository.TripSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripSegmentService {

    private final TripSegmentRepository tripSegmentRepository;
    private final TravelRepository travelRepository;
    private final BusRepository busRepository;
    private final DriverRepository driverRepository;
    private final TripSegmentMapper tripSegmentMapper;

    @Transactional(readOnly = true)
    public List<TripSegmentResponseDTO> getAll() {
        return tripSegmentRepository.findAll()
                .stream()
                .map(tripSegmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripSegmentResponseDTO getById(Long id) {
        TripSegment segment = tripSegmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l trayecto", id));
        return tripSegmentMapper.toDTO(segment);
    }

    @Transactional
    public TripSegmentResponseDTO create(TripSegmentRequestDTO dto) {
        Travel travel = travelRepository.findById(dto.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("l viaje", dto.getTravelId()));
        Bus bus = busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("l bus", dto.getBusId()));
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("l conductor", dto.getDriverId()));

        TripSegment segment = tripSegmentMapper.toEntity(dto, travel, bus, driver);
        return tripSegmentMapper.toDTO(tripSegmentRepository.save(segment));
    }

    @Transactional
    public TripSegmentResponseDTO update(Long id, TripSegmentRequestDTO dto) {
        TripSegment segment = tripSegmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l trayecto", id));

        Travel travel = travelRepository.findById(dto.getTravelId())
                .orElseThrow(() -> new ResourceNotFoundException("l viaje", dto.getTravelId()));
        Bus bus = busRepository.findById(dto.getBusId())
                .orElseThrow(() -> new ResourceNotFoundException("l bus", dto.getBusId()));
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("l conductor", dto.getDriverId()));

        segment.setTravel(travel);
        segment.setOrigin(dto.getOrigin());
        segment.setDestination(dto.getDestination());
        segment.setStartTime(dto.getStartTime());
        segment.setEndTime(dto.getEndTime());
        segment.setBus(bus);
        segment.setDriver(driver);
        return tripSegmentMapper.toDTO(tripSegmentRepository.save(segment));
    }

    @Transactional
    public void delete(Long id) {
        if (!tripSegmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("l trayecto", id);
        }
        tripSegmentRepository.deleteById(id);
    }
}