package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.model.Travel;
import com.inditex.g1_agencia_viajes.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    public List<Travel> findAll() {
        return travelRepository.findAll();
    }

    public Optional<Travel> findById(Long id) {
        return travelRepository.findById(id);
    }

    public Travel save(Travel travel) {
        return travelRepository.save(travel);
    }

    public Travel update(Long id, Travel travelDetails) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));
        travel.setDestiny(travelDetails.getDestiny());
        travel.setStartDate(travelDetails.getStartDate());
        travel.setEndDate(travelDetails.getEndDate());
        travel.setSale(travelDetails.getSale());
        travel.setAvailablePlaces(travelDetails.getAvailablePlaces());
        travel.setHotel(travelDetails.getHotel());
        travel.setOffer(travelDetails.getOffer());
        return travelRepository.save(travel);
    }

    public void deleteById(Long id) {
        travelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travel not found"));
        travelRepository.deleteById(id);
    }
}