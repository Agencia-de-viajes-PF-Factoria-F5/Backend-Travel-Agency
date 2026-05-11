package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {

    List<Travel> findByActiveTrue();

    List<Travel> findBySaleTrueAndActiveTrueAndStartDateAfter(LocalDate date);

    List<Travel> findByActiveTrueAndStartDateAfter(LocalDate date);
}