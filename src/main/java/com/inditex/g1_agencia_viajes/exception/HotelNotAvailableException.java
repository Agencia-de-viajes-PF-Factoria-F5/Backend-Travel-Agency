package com.inditex.g1_agencia_viajes.exception;

public class HotelNotAvailableException extends RuntimeException {
    public HotelNotAvailableException(Long id) {
        super("Hotel con id: " + id + " no tiene plazas disponibles");
    }
}