package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BusResponseDTO;
import com.inditex.g1_agencia_viajes.exception.DuplicateLicensePlateException;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BusResponseDTO> getAll() {
        return busRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BusResponseDTO getById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l bus", id));
        return toResponseDTO(bus);
    }

    @Override
    @Transactional
    public BusResponseDTO create(BusRequestDTO dto) {
        if (busRepository.existsByLicensePlate(dto.getLicensePlate())) {
            throw new DuplicateLicensePlateException(dto.getLicensePlate());
        }
        return toResponseDTO(busRepository.save(toEntity(dto)));
    }

    @Override
    @Transactional
    public BusResponseDTO update(Long id, BusRequestDTO dto) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l bus", id));
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setCapacity(dto.getCapacity());
        bus.setBath(dto.getBath());
        bus.setWifi(dto.getWifi());
        bus.setAC(dto.getAC());
        bus.setUSB(dto.getUSB());
        return toResponseDTO(busRepository.save(bus));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l bus", id));
        bus.setActive(false);
        busRepository.save(bus);
    }

    private BusResponseDTO toResponseDTO(Bus bus) {
        BusResponseDTO dto = new BusResponseDTO();
        dto.setId(bus.getId());
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setCapacity(bus.getCapacity());
        dto.setBath(bus.getBath());
        dto.setWifi(bus.getWifi());
        dto.setAC(bus.getAC());
        dto.setUSB(bus.getUSB());
        dto.setActive(bus.getActive());
        return dto;
    }

    private Bus toEntity(BusRequestDTO dto) {
        Bus bus = new Bus();
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setCapacity(dto.getCapacity());
        bus.setBath(dto.getBath());
        bus.setWifi(dto.getWifi());
        bus.setAC(dto.getAC());
        bus.setUSB(dto.getUSB());
        if (dto.getActive() != null) {
            bus.setActive(dto.getActive());
        }
        return bus;
    }
}