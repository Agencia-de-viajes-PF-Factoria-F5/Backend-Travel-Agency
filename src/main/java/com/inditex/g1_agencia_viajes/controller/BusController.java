package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.BusRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BusResponseDTO;
import com.inditex.g1_agencia_viajes.service.BusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buses")
@RequiredArgsConstructor
public class BusController {

    private final BusService busService;

    @GetMapping
    public ResponseEntity<List<BusResponseDTO>> getAll() {
        return ResponseEntity.ok(busService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BusResponseDTO> create(@Valid @RequestBody BusRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(busService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BusRequestDTO dto) {
        return ResponseEntity.ok(busService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        busService.delete(id);
        return ResponseEntity.noContent().build();
    }
}