package com.inditex.g1_agencia_viajes.exception;

public class

ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super("No hemos podido encontrar la información de"+ resourceName + ", con el id:"+id);
    }
}