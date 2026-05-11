package com.inditex.g1_agencia_viajes.exception;

public class TravelNotFoundException extends RuntimeException {
    public TravelNotFoundException(Long id) {
        super("Viaje no encontrado con id: " + id);
    }
}