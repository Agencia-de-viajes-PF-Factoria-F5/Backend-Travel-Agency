package com.inditex.g1_agencia_viajes.exception;

public class DriverOverlapException extends RuntimeException {
    public DriverOverlapException(Long driverId, String start, String end) {
        super("El conductor con id: " + driverId + " ya tiene un trayecto entre " + start + " y " + end);
    }
}
