package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BookingUserRequestDTO;
import com.inditex.g1_agencia_viajes.exception.MinorWithoutTutorException;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
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
        validateCustomersForBooking(booking.getCustomers());
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking update(Long id, Booking bookingDetails) {
        return bookingRepository.findById(id).map(booking -> {
            validateCustomersForBooking(bookingDetails.getCustomers());
            booking.setCustomers(bookingDetails.getCustomers());
            booking.setBoughtDate(bookingDetails.getBoughtDate());
            booking.setTypeBoard(bookingDetails.getTypeBoard());
            booking.setIsGroup(bookingDetails.getIsGroup());
            booking.setTotalPrice(bookingDetails.getTotalPrice());
            booking.setTravel(bookingDetails.getTravel());
            booking.setEmployee(bookingDetails.getEmployee());
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con el id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no encontrada con el id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void addCustomerToBooking(BookingUserRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        validateMinorHasTutor(user);
        booking.getCustomers().add(user);
        bookingRepository.save(booking);
    }

    private void validateCustomersForBooking(List<User> customers) {
        if (customers == null) {
            return;
        }

        for (User customer : customers) {
            validateMinorHasTutor(customer);
        }
    }

    private void validateMinorHasTutor(User user) {
        if (user != null
                && user.getAge() != null
                && user.getAge() < 18
                && user.getTutorId() == null) {
            throw new MinorWithoutTutorException();
        }
    }
}
