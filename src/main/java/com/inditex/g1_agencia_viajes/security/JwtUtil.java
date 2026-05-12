package com.inditex.g1_agencia_viajes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your_secret_password";

    // Aquí defines el algoritmo una sola vez
    private final Algorithm algoritmo = Algorithm.HMAC256(SECRET_KEY);

    public String crearToken(String email, Long id) {
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withIssuer("agencia-viajes")
                .sign(algoritmo); // Aquí usas el algoritmo para firmar
    }

    public Algorithm getAlgoritmo() {
        return algoritmo;
    }
}
