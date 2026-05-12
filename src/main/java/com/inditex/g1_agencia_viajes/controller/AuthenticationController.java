package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.LoginRequest;
import com.inditex.g1_agencia_viajes.dto.LoginResponse;
import com.inditex.g1_agencia_viajes.model.Employee;
import com.inditex.g1_agencia_viajes.repository.EmployeeRepository;
import com.inditex.g1_agencia_viajes.security.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Employee employee = employeeRepository.findById(loginRequest.getId()).orElse(null);

        if (employee != null && BCrypt.checkpw(loginRequest.getPassword(), employee.getPassword())) {
            String token = jwtUtil.crearToken(employee.getName(), employee.getEmployeeId());
            return ResponseEntity.ok(new LoginResponse(token, employee.getEmployeeId(), employee.getName(), employee.getSurname()));
        }

        return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
    }
}
