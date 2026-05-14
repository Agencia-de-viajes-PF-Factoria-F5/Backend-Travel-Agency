package com.inditex.g1_agencia_viajes.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingResponseDTO {

    private Long bookingId;
    private LocalDateTime boughtDate;
    private String typeBoard;
    private Boolean isGroup;
    private Double totalPrice;
    private Long travelId;
    private String travelDestiny;
    private List<Long> customerIds;
    private Long employeeId;
}
