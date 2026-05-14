package com.inditex.g1_agencia_viajes.exception;

public class TravelNotAvailableException extends RuntimeException {
    public TravelNotAvailableException(Long id) {
        super("El viaje con id: " + id + " no tiene plazas disponibles");
    }
}
