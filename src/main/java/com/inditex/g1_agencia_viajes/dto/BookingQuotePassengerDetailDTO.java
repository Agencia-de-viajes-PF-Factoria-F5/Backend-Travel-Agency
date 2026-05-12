package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

@Data
public class BookingQuotePassengerDetailDTO {

    private Long userId;
    private String fullName;
    private Integer age;
    private String category;
    private Double basePrice;
    private Double offerDiscountAmount;
    private Double categoryDiscountAmount;
    private Double finalPrice;
}
