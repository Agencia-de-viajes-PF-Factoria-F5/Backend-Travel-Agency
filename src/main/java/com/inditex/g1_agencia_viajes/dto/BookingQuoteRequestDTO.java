package com.inditex.g1_agencia_viajes.dto;

import com.inditex.g1_agencia_viajes.model.TypeBoard;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingQuoteRequestDTO {

    @NotNull(message = "El ID del viaje es obligatorio")
    private Long travelId;

    @NotNull(message = "El tipo de pensión es obligatorio")
    private TypeBoard typeBoard;

    private Boolean isGroup = false;

    @NotEmpty(message = "Debes indicar al menos un cliente")
    private List<Long> customerIds;
}
