package com.inditex.g1_agencia_viajes.controller;

import com.inditex.g1_agencia_viajes.model.Offer;
import com.inditex.g1_agencia_viajes.service.OfferService;
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
    public ResponseEntity<List<Offer>> getAll() {
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getById(@PathVariable Long id) {
        return offerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Offer> create(@RequestBody Offer offer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.save(offer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Offer> update(@PathVariable Long id, @RequestBody Offer offerDetails) {
        try {
            return ResponseEntity.ok(offerService.update(id, offerDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            offerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}