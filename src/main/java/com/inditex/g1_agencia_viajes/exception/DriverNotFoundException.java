package com.inditex.g1_agencia_viajes.exception;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException(Long id) {
        super("Conductor no encontrado con id: " + id);
    }
}