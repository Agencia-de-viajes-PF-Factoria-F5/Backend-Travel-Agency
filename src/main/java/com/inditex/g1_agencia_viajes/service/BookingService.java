package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BookingUserRequestDTO;
import com.inditex.g1_agencia_viajes.model.Booking;
import com.inditex.g1_agencia_viajes.model.User;
import com.inditex.g1_agencia_viajes.repository.BookingRepository;
import com.inditex.g1_agencia_viajes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking update(Long id, Booking bookingDetails) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setCustomers(bookingDetails.getCustomers());
            booking.setBoughtDate(bookingDetails.getBoughtDate());
            booking.setTypeBoard(bookingDetails.getTypeBoard());
            booking.setIsGroup(bookingDetails.getIsGroup());
            booking.setTotalPrice(bookingDetails.getTotalPrice());
            booking.setTravel(bookingDetails.getTravel());
            booking.setEmployee(bookingDetails.getEmployee());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Reserva no encontrada con el id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reserva no encontrada con el id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void addCustomerToBooking(BookingUserRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        booking.getCustomers().add(user);
        bookingRepository.save(booking);
    }
}