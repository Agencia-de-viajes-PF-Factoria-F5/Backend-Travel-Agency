package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.model.Offer;
import com.inditex.g1_agencia_viajes.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    private OfferService offerService;

    private Offer offer;

    @BeforeEach
    void setUp() {
        offerService = new OfferService(offerRepository);

        offer = new Offer();
        offer.setOfferId(1L);
        offer.setDiscountPercentage(20.0);
        offer.setStartDate(LocalDate.of(2026, 1, 1));
        offer.setEndDate(LocalDate.of(2026, 12, 31));
    }

    @Test
    void findAll_ShouldReturnAllOffers() {
        when(offerRepository.findAll()).thenReturn(List.of(offer));

        List<Offer> result = offerService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOfferId()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldReturnOffer() {
        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        Optional<Offer> result = offerService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getDiscountPercentage()).isEqualTo(20.0);
    }

    @Test
    void findById_ShouldReturnEmptyOptional() {
        when(offerRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Offer> result = offerService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldSaveOffer() {
        when(offerRepository.save(offer)).thenReturn(offer);

        Offer result = offerService.save(offer);

        assertThat(result).isNotNull();
        assertThat(result.getDiscountPercentage()).isEqualTo(20.0);
    }

    @Test
    void update_ShouldUpdateOffer() {
        Offer offerDetails = new Offer();
        offerDetails.setDiscountPercentage(30.0);
        offerDetails.setStartDate(LocalDate.of(2026, 3, 1));
        offerDetails.setEndDate(LocalDate.of(2026, 6, 30));

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        Offer result = offerService.update(1L, offerDetails);

        assertThat(result).isNotNull();
        assertThat(offer.getDiscountPercentage()).isEqualTo(30.0);
        assertThat(offer.getStartDate()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(offer.getEndDate()).isEqualTo(LocalDate.of(2026, 6, 30));
    }

    @Test
    void update_ShouldThrowRuntimeException() {
        when(offerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offerService.update(99L, new Offer()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Oferta no encontrada");
    }

    @Test
    void deleteById_ShouldDeleteOffer() {
        when(offerRepository.existsById(1L)).thenReturn(true);

        offerService.deleteById(1L);

        verify(offerRepository).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowRuntimeException() {
        when(offerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> offerService.deleteById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No se puede eliminar");
    }
}
