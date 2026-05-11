package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.TravelRequestDTO;
import com.inditex.g1_agencia_viajes.dto.TravelResponseDTO;
import com.inditex.g1_agencia_viajes.exception.TravelNotFoundException;
import com.inditex.g1_agencia_viajes.mapper.TravelMapper;
import com.inditex.g1_agencia_viajes.model.Hotel;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.repository.HotelRepository;
import com.inditex.g1_agencia_viajes.repository.TravelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelService {

    private final TravelRepository travelRepository;
    private final HotelRepository hotelRepository;
    private final TravelMapper travelMapper;

    public TravelService(TravelRepository travelRepository,
                         HotelRepository hotelRepository,
                         TravelMapper travelMapper) {
        this.travelRepository = travelRepository;
        this.hotelRepository = hotelRepository;
        this.travelMapper = travelMapper;
    }

    public List<TravelResponseDTO> getAll() {
        return travelRepository.findByActiveTrue()
                .stream()
                .map(travelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TravelResponseDTO> getAvailable() {
        return travelRepository.findByActiveTrueAndStartDateAfter(LocalDate.now())
                .stream()
                .map(travelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TravelResponseDTO> getOnSale() {
        return travelRepository.findBySaleTrueAndActiveTrueAndStartDateAfter(LocalDate.now())
                .stream()
                .map(travelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TravelResponseDTO getById(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new TravelNotFoundException(id));
        return travelMapper.toDTO(travel);
    }

    public TravelResponseDTO create(TravelRequestDTO dto) {
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con id: " + dto.getHotelId()));
        Travel travel = travelMapper.toEntity(dto, hotel);
        return travelMapper.toDTO(travelRepository.save(travel));
    }

    public TravelResponseDTO update(Long id, TravelRequestDTO dto) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new TravelNotFoundException(id));
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado con id: " + dto.getHotelId()));
        if (dto.getDestiny() != null)         travel.setDestiny(dto.getDestiny());
        if (dto.getStartDate() != null)       travel.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null)         travel.setEndDate(dto.getEndDate());
        if (dto.getSale() != null)            travel.setSale(dto.getSale());
        if (dto.getAvailablePlaces() != null) travel.setAvailablePlaces(dto.getAvailablePlaces());
        if (dto.getTags() != null)            travel.setTags(dto.getTags());
        travel.setHotel(hotel);
        return travelMapper.toDTO(travelRepository.save(travel));
    }

    public void delete(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new TravelNotFoundException(id));
        travel.setActive(false);
        travelRepository.save(travel);
    }
}
