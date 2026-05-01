package com.inditex.g1_agencia_viajes.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Usuario no encontrado con id: " + id);
    }
}