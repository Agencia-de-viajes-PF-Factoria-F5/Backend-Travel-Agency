package com.inditex.g1_agencia_viajes.exception;

public class MinorWithoutTutorException extends RuntimeException {
    public MinorWithoutTutorException() {
        super("Un menor de edad debe ir acompañado de un tutor para crear la reserva");
    }
}
