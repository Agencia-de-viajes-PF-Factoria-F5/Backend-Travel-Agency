package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.BusDTO;
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
@CrossOrigin(origins = "*")
public class BusController {

    private final BusService busService;

    // GET /api/buses
    @GetMapping
    public ResponseEntity<List<BusDTO>> getAll() {
        return ResponseEntity.ok(busService.getAll());
    }

    // GET /api/buses/1
    @GetMapping("/{id}")
    public ResponseEntity<BusDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.getById(id));
    }

    // POST /api/buses
    @PostMapping
    public ResponseEntity<BusDTO> create(@Valid @RequestBody BusDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(busService.create(dto));
    }

    // PUT /api/buses/1
    @PutMapping("/{id}")
    public ResponseEntity<BusDTO> update(@PathVariable Long id, @Valid @RequestBody BusDTO dto) {
        return ResponseEntity.ok(busService.update(id, dto));
    }

    // DELETE /api/buses/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        busService.delete(id);
        return ResponseEntity.noContent().build();
    }
}