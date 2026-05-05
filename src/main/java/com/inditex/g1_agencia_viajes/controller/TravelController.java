package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.TravelRequestDTO;
import com.inditex.g1_agencia_viajes.dto.TravelResponseDTO;
import com.inditex.g1_agencia_viajes.service.TravelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public ResponseEntity<List<TravelResponseDTO>> getAll() {
        return ResponseEntity.ok(travelService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<TravelResponseDTO>> getAvailable() {
        return ResponseEntity.ok(travelService.getAvailable());
    }

    @GetMapping("/sale")
    public ResponseEntity<List<TravelResponseDTO>> getOnSale() {
        return ResponseEntity.ok(travelService.getOnSale());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(travelService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TravelResponseDTO> create(@Valid @RequestBody TravelRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(travelService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody TravelRequestDTO dto) {
        return ResponseEntity.ok(travelService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        travelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}