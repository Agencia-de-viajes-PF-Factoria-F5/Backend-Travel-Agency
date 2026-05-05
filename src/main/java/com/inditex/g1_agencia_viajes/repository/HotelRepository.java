package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByActive(Boolean active);

    List<Hotel> findByCity(String city);

    List<Hotel> findByCountry(String country);

    List<Hotel> findByStars(Integer stars);

    List<Hotel> findByAvailablePlacesGreaterThan(Integer places);
}