package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
