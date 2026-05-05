package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {

    boolean existsByLicensePlate(String licensePlate);
}