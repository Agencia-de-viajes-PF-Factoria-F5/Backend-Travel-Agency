package com.inditex.g1_agencia_viajes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.inditex.g1_agencia_viajes.model.Role;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your_secret_password";

    private final Algorithm algoritmo = Algorithm.HMAC256(SECRET_KEY);

    public String crearToken(String email, Long id, Role role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("id", id)
                .withClaim("role", role.name())
                .withIssuer("agencia-viajes")
                .sign(algoritmo);
    }

    public Algorithm getAlgoritmo() {
        return algoritmo;
    }
}
