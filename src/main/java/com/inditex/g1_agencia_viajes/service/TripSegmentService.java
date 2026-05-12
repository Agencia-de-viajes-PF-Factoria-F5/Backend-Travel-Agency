package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.TripSegment;
import com.inditex.g1_agencia_viajes.repository.TripSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripSegmentService {

    private final TripSegmentRepository tripSegmentRepository;

    @Transactional(readOnly = true)
    public List<TripSegment> getAll() {
        return tripSegmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TripSegment getById(Long id) {
        return tripSegmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l trayecto", id));
    }

    @Transactional
    public TripSegment create(TripSegment tripSegment) {
        validateRelations(tripSegment);
        return tripSegmentRepository.save(tripSegment);
    }

    @Transactional
    public TripSegment update(Long id, TripSegment tripSegmentDetails) {
        TripSegment tripSegment = getById(id);
        tripSegment.setTravel(tripSegmentDetails.getTravel());
        tripSegment.setOrigin(tripSegmentDetails.getOrigin());
        tripSegment.setDestination(tripSegmentDetails.getDestination());
        tripSegment.setStartTime(tripSegmentDetails.getStartTime());
        tripSegment.setEndTime(tripSegmentDetails.getEndTime());
        tripSegment.setBus(tripSegmentDetails.getBus());
        tripSegment.setDriver(tripSegmentDetails.getDriver());
        validateRelations(tripSegment);
        return tripSegmentRepository.save(tripSegment);
    }

    @Transactional
    public void delete(Long id) {
        if (!tripSegmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("l trayecto", id);
        }
        tripSegmentRepository.deleteById(id);
    }

    private void validateRelations(TripSegment tripSegment) {
        if (tripSegment.getTravel() == null || tripSegment.getTravel().getId() == null) {
            throw new IllegalArgumentException("Travel is required");
        }
        if (tripSegment.getBus() == null || tripSegment.getBus().getId() == null) {
            throw new IllegalArgumentException("Bus is required");
        }
        if (tripSegment.getDriver() == null || tripSegment.getDriver().getId() == null) {
            throw new IllegalArgumentException("Driver is required");
        }
    }
}