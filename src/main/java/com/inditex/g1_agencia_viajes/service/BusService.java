package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BusResponseDTO;

import java.util.List;

public interface BusService {

    List<BusResponseDTO> getAll();
    BusResponseDTO getById(Long id);
    BusResponseDTO create(BusRequestDTO dto);
    BusResponseDTO update(Long id, BusRequestDTO dto);
    void delete(Long id);
}