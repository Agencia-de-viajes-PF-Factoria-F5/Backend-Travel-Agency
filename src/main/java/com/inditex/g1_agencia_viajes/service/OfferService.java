package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.model.Offer;
import com.inditex.g1_agencia_viajes.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final OfferRepository offerRepository;

    @Transactional(readOnly = true)
    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }

    @Transactional
    public Offer save(Offer offer) {
        return offerRepository.save(offer);
    }

    @Transactional
    public Offer update(Long id, Offer offerDetails) {
        return offerRepository.findById(id).map(offer -> {
            offer.setDiscountPercentage(offerDetails.getDiscountPercentage());
            offer.setStartDate(offerDetails.getStartDate());
            offer.setEndDate(offerDetails.getEndDate());
            return offerRepository.save(offer);
        }).orElseThrow(() -> new RuntimeException("Oferta no encontrada con el id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Oferta no encontrada con el id: " + id);
        }
        offerRepository.deleteById(id);
    }
}