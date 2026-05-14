package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.OfferRequestDTO;
import com.inditex.g1_agencia_viajes.dto.OfferResponseDTO;
import com.inditex.g1_agencia_viajes.model.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferMapper {

    public Offer toEntity(OfferRequestDTO dto) {
        Offer offer = new Offer();
        offer.setDiscountPercentage(dto.getDiscountPercentage());
        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        return offer;
    }

    public OfferResponseDTO toDTO(Offer offer) {
        OfferResponseDTO dto = new OfferResponseDTO();
        dto.setOfferId(offer.getOfferId());
        dto.setDiscountPercentage(offer.getDiscountPercentage());
        dto.setStartDate(offer.getStartDate());
        dto.setEndDate(offer.getEndDate());
        return dto;
    }
}
