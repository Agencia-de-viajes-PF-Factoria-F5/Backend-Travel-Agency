package com.inditex.g1_agencia_viajes.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // 1. Permitir el login sin token
        if (path.equals("/api/authentication/login")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Obtener el token de la cabecera "Authorization"
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ausente o inválido");
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 3. Validar el token
            Algorithm algoritmo = Algorithm.HMAC256("your_secret_password");
            JWTVerifier verifier = JWT.require(algoritmo).withIssuer("agencia-viajes").build();
            DecodedJWT jwt = verifier.verify(token);

            // Opcional: Guardar los datos del empleado en el request por si el controlador los necesita
            req.setAttribute("id", jwt.getClaim("id").asLong());

            chain.doFilter(request, response); // Todo OK, continúa a tu Controlador
        } catch (JWTVerificationException exception) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
        }
    }
}
