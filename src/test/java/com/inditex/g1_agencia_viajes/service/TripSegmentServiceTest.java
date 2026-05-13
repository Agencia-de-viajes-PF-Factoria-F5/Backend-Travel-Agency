package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.model.Driver;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.model.TripSegment;
import com.inditex.g1_agencia_viajes.repository.TripSegmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripSegmentServiceTest {

    @Mock
    private TripSegmentRepository tripSegmentRepository;

    private TripSegmentService tripSegmentService;

    private TripSegment tripSegment;
    private Travel travel;
    private Bus bus;
    private Driver driver;

    @BeforeEach
    void setUp() {
        tripSegmentService = new TripSegmentService(tripSegmentRepository);

        travel = new Travel();
        travel.setId(1L);

        bus = new Bus();
        bus.setId(1L);

        driver = new Driver();
        driver.setId(1L);

        tripSegment = new TripSegment();
        tripSegment.setId(1L);
        tripSegment.setTravel(travel);
        tripSegment.setOrigin("Madrid");
        tripSegment.setDestination("Barcelona");
        tripSegment.setStartTime(LocalDateTime.of(2026, 6, 15, 8, 0));
        tripSegment.setEndTime(LocalDateTime.of(2026, 6, 15, 12, 0));
        tripSegment.setBus(bus);
        tripSegment.setDriver(driver);
    }

    @Test
    void getAll_ShouldReturnAllSegments() {
        when(tripSegmentRepository.findAll()).thenReturn(List.of(tripSegment));

        List<TripSegment> result = tripSegmentService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrigin()).isEqualTo("Madrid");
    }

    @Test
    void getById_ShouldReturnSegment() {
        when(tripSegmentRepository.findById(1L)).thenReturn(Optional.of(tripSegment));

        TripSegment result = tripSegmentService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_ShouldThrowResourceNotFoundException() {
        when(tripSegmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tripSegmentService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ShouldCreateSegment() {
        when(tripSegmentRepository.save(tripSegment)).thenReturn(tripSegment);

        TripSegment result = tripSegmentService.create(tripSegment);

        assertThat(result).isNotNull();
    }

    @Test
    void create_ShouldThrowWhenTravelIsNull() {
        tripSegment.setTravel(null);

        assertThatThrownBy(() -> tripSegmentService.create(tripSegment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Travel is required");
    }

    @Test
    void create_ShouldThrowWhenTravelIdIsNull() {
        tripSegment.getTravel().setId(null);

        assertThatThrownBy(() -> tripSegmentService.create(tripSegment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Travel is required");
    }

    @Test
    void create_ShouldThrowWhenBusIsNull() {
        tripSegment.setBus(null);

        assertThatThrownBy(() -> tripSegmentService.create(tripSegment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Bus is required");
    }

    @Test
    void create_ShouldThrowWhenDriverIsNull() {
        tripSegment.setDriver(null);

        assertThatThrownBy(() -> tripSegmentService.create(tripSegment))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Driver is required");
    }

    @Test
    void update_ShouldUpdateSegment() {
        TripSegment segmentDetails = new TripSegment();
        segmentDetails.setTravel(travel);
        segmentDetails.setOrigin("Barcelona");
        segmentDetails.setDestination("Madrid");
        segmentDetails.setStartTime(LocalDateTime.of(2026, 6, 16, 8, 0));
        segmentDetails.setEndTime(LocalDateTime.of(2026, 6, 16, 12, 0));
        segmentDetails.setBus(bus);
        segmentDetails.setDriver(driver);

        when(tripSegmentRepository.findById(1L)).thenReturn(Optional.of(tripSegment));
        when(tripSegmentRepository.save(any(TripSegment.class))).thenReturn(tripSegment);

        TripSegment result = tripSegmentService.update(1L, segmentDetails);

        assertThat(result).isNotNull();
    }

    @Test
    void delete_ShouldDeleteSegment() {
        when(tripSegmentRepository.existsById(1L)).thenReturn(true);

        tripSegmentService.delete(1L);

        verify(tripSegmentRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException() {
        when(tripSegmentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tripSegmentService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
