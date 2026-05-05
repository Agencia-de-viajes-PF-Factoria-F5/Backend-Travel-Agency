package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.DriverRequestDTO;
import com.inditex.g1_agencia_viajes.dto.DriverResponseDTO;
import com.inditex.g1_agencia_viajes.exception.DriverNotFoundException;
import com.inditex.g1_agencia_viajes.mapper.DriverMapper;
import com.inditex.g1_agencia_viajes.model.Driver;
import com.inditex.g1_agencia_viajes.repository.DriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    public DriverResponseDTO create(DriverRequestDTO dto) {
        Driver driver = driverMapper.toEntity(dto);
        return driverMapper.toDTO(driverRepository.save(driver));
    }

    public List<DriverResponseDTO> getAll() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DriverResponseDTO getById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        return driverMapper.toDTO(driver);
    }

    public List<DriverResponseDTO> getActive() {
        return driverRepository.findByLicenceActive(true)
                .stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DriverResponseDTO update(Long id, DriverRequestDTO dto) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        if (dto.getName() != null)         driver.setName(dto.getName());
        if (dto.getPhone() != null)        driver.setPhone(dto.getPhone());
        if (dto.getEnrollment() != null)   driver.setEnrollment(dto.getEnrollment());
        if (dto.getImageUrl() != null)     driver.setImageUrl(dto.getImageUrl());
        if (dto.getLicenceActive() != null) driver.setLicenceActive(dto.getLicenceActive());
        return driverMapper.toDTO(driverRepository.save(driver));
    }

    public void delete(Long id) {
        if (!driverRepository.existsById(id)) {
            throw new DriverNotFoundException(id);
        }
        driverRepository.deleteById(id);
    }
}