package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BusDTO;
import java.util.List;

public interface BusService {

    List<BusDTO> getAll();
    BusDTO getById(Long id);
    BusDTO create(BusDTO dto);
    BusDTO update(Long id, BusDTO dto);
    void delete(Long id);
}