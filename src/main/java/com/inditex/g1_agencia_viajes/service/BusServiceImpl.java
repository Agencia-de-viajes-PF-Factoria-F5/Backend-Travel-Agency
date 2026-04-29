package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusDTO;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Override
    public List<BusDTO> getAll() {
        return busRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BusDTO getById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autobús no encontrado con id: " + id));
        return toDTO(bus);
    }

    @Override
    public BusDTO create(BusDTO dto) {
        if (busRepository.existsByLicensePlate(dto.getLicensePlate())) {
            throw new RuntimeException("Ya existe un autobús con la matrícula: " + dto.getLicensePlate());
        }
        Bus bus = toEntity(dto);
        return toDTO(busRepository.save(bus));
    }

    @Override
    public BusDTO update(Long id, BusDTO dto) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autobús no encontrado con id: " + id));
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setBrand(dto.getBrand());
        bus.setCapacity(dto.getCapacity());
        bus.setYear(dto.getYear());
        bus.setAvailable(dto.getAvailable());
        return toDTO(busRepository.save(bus));
    }

    @Override
    public void delete(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autobús no encontrado con id: " + id);
        }
        busRepository.deleteById(id);
    }

    // ── Mappers ──────────────────────────────────────────
    private BusDTO toDTO(Bus bus) {
        BusDTO dto = new BusDTO();
        dto.setId(bus.getId());
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setBrand(bus.getBrand());
        dto.setCapacity(bus.getCapacity());
        dto.setYear(bus.getYear());
        dto.setAvailable(bus.getAvailable());
        return dto;
    }

    private Bus toEntity(BusDTO dto) {
        Bus bus = new Bus();
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setBrand(dto.getBrand());
        bus.setCapacity(dto.getCapacity());
        bus.setYear(dto.getYear());
        bus.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        return bus;
    }
}