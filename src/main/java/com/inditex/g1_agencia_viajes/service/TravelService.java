package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.TravelRequestDTO;
import com.inditex.g1_agencia_viajes.dto.TravelResponseDTO;
import com.inditex.g1_agencia_viajes.mapper.TravelMapper;
import com.inditex.g1_agencia_viajes.model.Hotel;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.repository.HotelRepository;
import com.inditex.g1_agencia_viajes.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    public List<Travel> findAll() {
        return travelRepository.findAll();
    @Transactional(readOnly = true)
    public List<TravelResponseDTO> getAvailable() {
        return travelRepository.findAll()
                .stream()
                .filter(t -> t.getAvailablePlaces() != null && t.getAvailablePlaces() > 0)
                .filter(t -> t.getStartDate().isAfter(LocalDate.now()))
                .map(travelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<Travel> findById(Long id) {
        return travelRepository.findById(id);
    @Transactional(readOnly = true)
    public List<TravelResponseDTO> getOnSale() {
        return travelRepository.findAll()
                .stream()
                .filter(t -> Boolean.TRUE.equals(t.getSale()))
                .map(travelMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Travel save(Travel travel) {
        return travelRepository.save(travel);
    @Transactional(readOnly = true)
    public TravelResponseDTO getById(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));
        return travelMapper.toDTO(travel);
    }

    public Travel update(Long id, Travel travelDetails) {
    @Transactional
    public TravelResponseDTO create(TravelRequestDTO dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate()) ||
                dto.getEndDate().isEqual(dto.getStartDate())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel no encontrado"));
        Travel travel = travelMapper.toEntity(dto, hotel);
        return travelMapper.toDTO(travelRepository.save(travel));
    }

    @Transactional
    public TravelResponseDTO update(Long id, TravelRequestDTO dto) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));
        travel.setDestiny(travelDetails.getDestiny());
        travel.setStartDate(travelDetails.getStartDate());
        travel.setEndDate(travelDetails.getEndDate());
        travel.setSale(travelDetails.getSale());
        travel.setAvailablePlaces(travelDetails.getAvailablePlaces());
        travel.setHotel(travelDetails.getHotel());
        travel.setOffer(travelDetails.getOffer());
        return travelRepository.save(travel);
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));
        if (dto.getEndDate().isBefore(dto.getStartDate()) ||
                dto.getEndDate().isEqual(dto.getStartDate())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        Hotel hotel = hotelRepository.findById(dto.getHotelId())
                .orElseThrow(() -> new IllegalArgumentException("Hotel no encontrado"));
        travel.setDestiny(dto.getDestiny());
        travel.setStartDate(dto.getStartDate());
        travel.setEndDate(dto.getEndDate());
        travel.setSale(dto.getSale());
        travel.setAvailablePlaces(dto.getAvailablePlaces());
        travel.setHotel(hotel);
        return travelMapper.toDTO(travelRepository.save(travel));
    }

    public void deleteById(Long id) {
        travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));
        travelRepository.deleteById(id);
    @Transactional
    public void delete(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado con id: " + id));
        travel.setActive(false);
        travelRepository.save(travel);
    }
}