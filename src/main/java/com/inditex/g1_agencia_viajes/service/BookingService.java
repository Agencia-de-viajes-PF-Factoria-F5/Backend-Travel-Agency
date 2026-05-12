package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BookingUserRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BookingQuoteRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BookingQuoteResponseDTO;
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
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingPricingService bookingPricingService;

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
        List<User> resolvedCustomers = resolveCustomers(booking.getCustomers());
        validateCustomersForBooking(resolvedCustomers);
        booking.setCustomers(resolvedCustomers);
        booking.setTotalPrice(bookingPricingService.calculateTotalPrice(booking));
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking update(Long id, Booking bookingDetails) {
        return bookingRepository.findById(id).map(booking -> {
            List<User> resolvedCustomers = resolveCustomers(bookingDetails.getCustomers());
            validateCustomersForBooking(resolvedCustomers);
            booking.setCustomers(resolvedCustomers);
            booking.setBoughtDate(bookingDetails.getBoughtDate());
            booking.setTypeBoard(bookingDetails.getTypeBoard());
            booking.setIsGroup(bookingDetails.getIsGroup());
            booking.setTravel(bookingDetails.getTravel());
            booking.setEmployee(bookingDetails.getEmployee());
            booking.setTotalPrice(bookingPricingService.calculateTotalPrice(booking));
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new ResourceNotFoundException(" la reserva", id));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException(" la reserva", id);
        }
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void addCustomerToBooking(BookingUserRequestDTO request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(" la reserva", request.getBookingId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("l usuario", request.getUserId()));
        validateMinorHasTutor(user);
        booking.getCustomers().add(user);
        booking.setTotalPrice(bookingPricingService.calculateTotalPrice(booking));
        bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public BookingQuoteResponseDTO quote(BookingQuoteRequestDTO request) {
        return bookingPricingService.quote(request);
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

    private List<User> resolveCustomers(List<User> customers) {
        if (customers == null) {
            return null;
        }

        List<User> resolvedCustomers = new ArrayList<>();
        for (User customer : customers) {
            if (customer == null || customer.getId() == null) {
                throw new IllegalArgumentException("Cada cliente debe incluir un ID");
            }

            User resolvedCustomer = userRepository.findById(customer.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("l cliente", customer.getId()));
            resolvedCustomers.add(resolvedCustomer);
        }
        return resolvedCustomers;
    }
}
