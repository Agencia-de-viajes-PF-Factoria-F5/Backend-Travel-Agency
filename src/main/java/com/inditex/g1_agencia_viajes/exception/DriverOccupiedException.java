package com.inditex.g1_agencia_viajes.exception;

public class DriverOccupiedException extends RuntimeException {
    public DriverOccupiedException(Long id) {
        super("Conductor con id: " + id + " ya está asignado a un autobús");
    }
}