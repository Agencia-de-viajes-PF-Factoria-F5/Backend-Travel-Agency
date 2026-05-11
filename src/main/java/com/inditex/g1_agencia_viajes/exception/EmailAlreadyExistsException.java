package com.inditex.g1_agencia_viajes.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Ya existe un usuario con el email: " + email);
    }
}