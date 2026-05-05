package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BookingUserRequestDTO;
import com.inditex.g1_agencia_viajes.model.Booking;
import com.inditex.g1_agencia_viajes.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

    @Service
    public class BookingService {

        @Autowired
        private BookingRepository bookingRepository;

        public List<Booking> findAll() {
            return bookingRepository.findAll();
        }

        public Optional<Booking> findById(Long id) {
            return bookingRepository.findById(id);
        }

        public Booking save(Booking booking) {
            return bookingRepository.save(booking);
        }

        public Booking update(Long id, Booking bookingDetails) {
            return bookingRepository.findById(id).map(booking -> {
                booking.setUserId(bookingDetails.getUserId());
                booking.setBoughtDate(bookingDetails.getBoughtDate());
                booking.setTypeBoard(bookingDetails.getTypeBoard());
                booking.setGroup(bookingDetails.getGroup());
                booking.setTotalPrice(bookingDetails.getTotalPrice());
                booking.setTravel(bookingDetails.getTravel());
                return bookingRepository.save(booking);
            }).orElseThrow(() -> new RuntimeException("Reserva no encontrada con el id: " + id));
        }

        public void deleteById(Long id) {
            if (!bookingRepository.existsById(id)) {
                throw new RuntimeException("No se puede eliminar. Reserva no encontrada con el id: " + id);
            }
            bookingRepository.deleteById(id);
        }
        public void addCustomerToBooking(BookingUserRequestDTO request) {
            Booking booking = bookingRepository.findById(request.getBookingId())
                    .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            booking.getCustomers().add(user);

            bookingRepository.save(booking);
        }
    }
}
