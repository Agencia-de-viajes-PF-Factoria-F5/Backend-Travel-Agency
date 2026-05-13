package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BusResponseDTO;
import com.inditex.g1_agencia_viajes.dto.DriverSummaryDTO;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.mapper.DriverMapper;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.model.Driver;
import com.inditex.g1_agencia_viajes.repository.BusRepository;
import com.inditex.g1_agencia_viajes.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusServiceImplTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    private BusServiceImpl busService;

    private Bus bus;
    private Driver driver;
    private BusRequestDTO requestDTO;
    private DriverSummaryDTO driverSummaryDTO;

    @BeforeEach
    void setUp() {
        busService = new BusServiceImpl(busRepository, driverRepository, driverMapper);

        driver = new Driver();
        driver.setId(1L);
        driver.setName("Driver Test");
        driver.setPhone("123456789");

        driverSummaryDTO = new DriverSummaryDTO();
        driverSummaryDTO.setId(1L);
        driverSummaryDTO.setName("Driver Test");
        driverSummaryDTO.setPhone("123456789");

        bus = new Bus();
        bus.setId(1L);
        bus.setLicensePlate("ABC-1234");
        bus.setCapacity(50);
        bus.setBath(true);
        bus.setWifi(true);
        bus.setAC(true);
        bus.setUSB(true);
        bus.setDriver(driver);

        requestDTO = new BusRequestDTO();
        requestDTO.setLicensePlate("ABC-1234");
        requestDTO.setCapacity(50);
        requestDTO.setBath(true);
        requestDTO.setWifi(true);
        requestDTO.setAC(true);
        requestDTO.setUSB(true);
        requestDTO.setDriverId(1L);
    }

    @Test
    void getAll_ShouldReturnListOfBuses() {
        when(busRepository.findAll()).thenReturn(List.of(bus));
        when(driverMapper.toSummaryDTO(driver)).thenReturn(driverSummaryDTO);

        List<BusResponseDTO> result = busService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLicensePlate()).isEqualTo("ABC-1234");
    }

    @Test
    void getAll_WithNullDriver_ShouldHandleGracefully() {
        bus.setDriver(null);
        when(busRepository.findAll()).thenReturn(List.of(bus));
        when(driverMapper.toSummaryDTO(null)).thenReturn(null);

        List<BusResponseDTO> result = busService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDriver()).isNull();
    }

    @Test
    void getById_ShouldReturnBus() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverMapper.toSummaryDTO(driver)).thenReturn(driverSummaryDTO);

        BusResponseDTO result = busService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_ShouldThrowResourceNotFoundException() {
        when(busRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> busService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ShouldCreateBus() {
        when(busRepository.existsByLicensePlate("ABC-1234")).thenReturn(false);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(busRepository.save(any(Bus.class))).thenReturn(bus);
        when(driverMapper.toSummaryDTO(driver)).thenReturn(driverSummaryDTO);

        BusResponseDTO result = busService.create(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getLicensePlate()).isEqualTo("ABC-1234");
    }

    @Test
    void create_ShouldThrowWhenLicensePlateExists() {
        when(busRepository.existsByLicensePlate("ABC-1234")).thenReturn(true);

        assertThatThrownBy(() -> busService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe un autobús");
    }

    @Test
    void update_ShouldUpdateBus() {
        BusRequestDTO updateDTO = new BusRequestDTO();
        updateDTO.setLicensePlate("XYZ-5678");
        updateDTO.setCapacity(40);
        updateDTO.setBath(false);
        updateDTO.setWifi(false);
        updateDTO.setAC(true);
        updateDTO.setUSB(false);
        updateDTO.setDriverId(1L);

        when(busRepository.findById(1L)).thenReturn(Optional.of(bus));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(busRepository.save(any(Bus.class))).thenReturn(bus);
        when(driverMapper.toSummaryDTO(driver)).thenReturn(driverSummaryDTO);

        BusResponseDTO result = busService.update(1L, updateDTO);

        assertThat(result).isNotNull();
    }

    @Test
    void update_ShouldThrowResourceNotFoundException() {
        when(busRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> busService.update(99L, new BusRequestDTO()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_ShouldDeleteBus() {
        when(busRepository.existsById(1L)).thenReturn(true);

        busService.delete(1L);

        verify(busRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException() {
        when(busRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> busService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
