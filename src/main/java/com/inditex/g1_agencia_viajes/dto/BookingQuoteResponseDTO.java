package com.inditex.g1_agencia_viajes.dto;

import com.inditex.g1_agencia_viajes.model.TypeBoard;
import lombok.Data;

import java.util.List;

@Data
public class BookingQuoteResponseDTO {

    private Long travelId;
    private String travelDestiny;
    private TypeBoard typeBoard;
    private Boolean isGroup;
    private Integer passengers;
    private Double basePricePerPassenger;
    private Double totalBeforeDiscount;
    private Double totalDiscount;
    private Double totalPrice;
    private List<BookingQuotePassengerDetailDTO> passengerDetails;
}
