package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

@Data
public class HotelResponseDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Integer stars;
    private Integer capacity;
    private Integer availablePlaces;
    private Double halfBoardPrice;
    private Double fullBoardPrice;
    private String imageUrl;
    private Boolean active;
}