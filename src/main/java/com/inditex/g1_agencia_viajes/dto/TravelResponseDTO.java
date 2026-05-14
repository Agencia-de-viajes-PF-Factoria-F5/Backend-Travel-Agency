package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TravelResponseDTO {
    private Long id;
    private String destiny;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean sale;
    private Integer availablePlaces;
    private String hotelName;
    private String hotelCity;
    private String hotelImageUrl;
    private Integer hotelStars;
    private Double discountPercentage;
    private Double halfBoardPrice;
    private Double fullBoardPrice;
}