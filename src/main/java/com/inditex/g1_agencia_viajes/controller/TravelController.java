package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.TravelResponseDTO;
import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @GetMapping
    public ResponseEntity<List<TravelResponseDTO>> getAll() {
        List<TravelResponseDTO> travels = travelService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(travels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> getById(@PathVariable Long id) {
        return travelService.findById(id)
                .map(t -> new ResponseEntity<>(toDTO(t), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Travel> create(@RequestBody Travel travel) {
        return new ResponseEntity<>(travelService.save(travel), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Travel> update(@PathVariable Long id, @RequestBody Travel travelDetails) {
        try {
            return new ResponseEntity<>(travelService.update(id, travelDetails), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            travelService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private TravelResponseDTO toDTO(Travel t) {
        TravelResponseDTO dto = new TravelResponseDTO();
        dto.setId(t.getId());
        dto.setDestiny(t.getDestiny());
        dto.setStartDate(t.getStartDate());
        dto.setEndDate(t.getEndDate());
        dto.setSale(t.getSale());
        dto.setAvailablePlaces(t.getAvailablePlaces());
        if (t.getHotel() != null) {
            dto.setHotelName(t.getHotel().getName());
            dto.setHotelCity(t.getHotel().getCity());
            dto.setHotelImageUrl(t.getHotel().getImageUrl());
            dto.setHotelStars(t.getHotel().getStars());
            dto.setHalfBoardPrice(t.getHotel().getHalfBoardPrice());
            dto.setFullBoardPrice(t.getHotel().getFullBoardPrice());
        }
        if (t.getOffer() != null) {
            dto.setDiscountPercentage(t.getOffer().getDiscountPercentage());
        }
        return dto;
    }
}
