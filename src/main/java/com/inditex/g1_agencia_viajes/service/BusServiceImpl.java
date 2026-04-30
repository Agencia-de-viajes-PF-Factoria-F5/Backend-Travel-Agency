package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BusResponseDTO;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.mapper.ConductorMapper;
import com.inditex.g1_agencia_viajes.model.Bus;
import com.inditex.g1_agencia_viajes.model.Conductor;
import com.inditex.g1_agencia_viajes.repository.BusRepository;
import com.inditex.g1_agencia_viajes.repository.ConductorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;
    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

    @Override
    public List<BusResponseDTO> getAll() {
        return busRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BusResponseDTO getById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autobús no encontrado con id: " + id));
        return toResponseDTO(bus);
    }

    @Override
    public BusResponseDTO create(BusRequestDTO dto) {
        if (busRepository.existsByLicensePlate(dto.getLicensePlate())) {
            throw new RuntimeException("Ya existe un autobús con la matrícula: " + dto.getLicensePlate());
        }
        return toResponseDTO(busRepository.save(toEntity(dto)));
    }

    @Override
    public BusResponseDTO update(Long id, BusRequestDTO dto) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autobús no encontrado con id: " + id));
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setBrand(dto.getBrand());
        bus.setCapacity(dto.getCapacity());
        bus.setYear(dto.getYear());
        bus.setConductor(resolveConductor(dto.getConductorId()));
        return toResponseDTO(busRepository.save(bus));
    }

    @Override
    public void delete(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autobús no encontrado con id: " + id);
        }
        busRepository.deleteById(id);
    }

    // ── Mappers ──────────────────────────────────────────
    private BusResponseDTO toResponseDTO(Bus bus) {
        BusResponseDTO dto = new BusResponseDTO();
        dto.setId(bus.getId());
        dto.setLicensePlate(bus.getLicensePlate());
        dto.setBrand(bus.getBrand());
        dto.setCapacity(bus.getCapacity());
        dto.setYear(bus.getYear());
        dto.setAvailable(bus.getAvailable());
        dto.setConductor(conductorMapper.toResumenDTO(bus.getConductor()));
        return dto;
    }

    private Bus toEntity(BusRequestDTO dto) {
        Bus bus = new Bus();
        bus.setLicensePlate(dto.getLicensePlate());
        bus.setBrand(dto.getBrand());
        bus.setCapacity(dto.getCapacity());
        bus.setYear(dto.getYear());
        bus.setConductor(resolveConductor(dto.getConductorId()));
        return bus;
    }

    private Conductor resolveConductor(Long conductorId) {
        if (conductorId == null) return null;
        return conductorRepository.findById(conductorId)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con id: " + conductorId));
    }
}