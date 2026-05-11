package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findByLicenceActive(boolean active);
}