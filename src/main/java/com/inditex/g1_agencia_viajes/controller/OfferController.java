package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.dto.OfferRequestDTO;
import com.inditex.g1_agencia_viajes.dto.OfferResponseDTO;
import com.inditex.g1_agencia_viajes.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<OfferResponseDTO>> getAll() {
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferResponseDTO> getById(@PathVariable Long id) {
        return offerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OfferResponseDTO> create(@Valid @RequestBody OfferRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody OfferRequestDTO dto) {
        return ResponseEntity.ok(offerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        offerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}