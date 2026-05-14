package com.inditex.g1_agencia_viajes.exception;

public class DuplicateLicensePlateException extends RuntimeException {
    public DuplicateLicensePlateException(String licensePlate) {
        super("Ya existe un autobús con la matrícula: " + licensePlate);
    }
}
