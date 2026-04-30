package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {
}