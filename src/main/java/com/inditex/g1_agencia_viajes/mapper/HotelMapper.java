package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.HotelRequestDTO;
import com.inditex.g1_agencia_viajes.dto.HotelResponseDTO;
import com.inditex.g1_agencia_viajes.model.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    public Hotel toEntity(HotelRequestDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setCity(dto.getCity());
        hotel.setCountry(dto.getCountry());
        hotel.setStars(dto.getStars());
        hotel.setCapacity(dto.getCapacity());
        hotel.setAvailablePlaces(dto.getAvailablePlaces());
        hotel.setHalfBoardPrice(dto.getHalfBoardPrice());
        hotel.setFullBoardPrice(dto.getFullBoardPrice());
        hotel.setImageUrl(dto.getImageUrl());
        if (dto.getActive() != null) {
            hotel.setActive(dto.getActive());
        }
        return hotel;
    }

    public HotelResponseDTO toDTO(Hotel hotel) {
        HotelResponseDTO dto = new HotelResponseDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setAddress(hotel.getAddress());
        dto.setCity(hotel.getCity());
        dto.setCountry(hotel.getCountry());
        dto.setStars(hotel.getStars());
        dto.setCapacity(hotel.getCapacity());
        dto.setAvailablePlaces(hotel.getAvailablePlaces());
        dto.setHalfBoardPrice(hotel.getHalfBoardPrice());
        dto.setFullBoardPrice(hotel.getFullBoardPrice());
        dto.setImageUrl(hotel.getImageUrl());
        dto.setActive(hotel.getActive());
        return dto;
    }
}