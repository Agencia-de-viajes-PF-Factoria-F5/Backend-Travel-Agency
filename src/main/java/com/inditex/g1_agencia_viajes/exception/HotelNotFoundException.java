package com.inditex.g1_agencia_viajes.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Long id) {
        super("Hotel no encontrado con id: " + id);
    }
}