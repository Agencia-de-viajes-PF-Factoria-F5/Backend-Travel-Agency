package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.dto.BookingQuoteRequestDTO;
import com.inditex.g1_agencia_viajes.dto.BookingQuoteResponseDTO;
import com.inditex.g1_agencia_viajes.dto.BookingUserRequestDTO;
import com.inditex.g1_agencia_viajes.exception.MinorWithoutTutorException;
import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.*;
import com.inditex.g1_agencia_viajes.repository.BookingRepository;
import com.inditex.g1_agencia_viajes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingPricingService bookingPricingService;

    private BookingService bookingService;

    private Booking booking;
    private User adultUser;
    private User minorWithoutTutor;
    private User minorWithTutor;
    private User tutor;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepository, userRepository, bookingPricingService);

        tutor = new User();
        tutor.setId(1L);
        tutor.setName("Tutor");
        tutor.setSurname("Test");
        tutor.setAge(30);
        tutor.setEmail("tutor@test.com");

        adultUser = new User();
        adultUser.setId(2L);
        adultUser.setName("Adult");
        adultUser.setSurname("User");
        adultUser.setAge(25);
        adultUser.setEmail("adult@test.com");

        minorWithTutor = new User();
        minorWithTutor.setId(3L);
        minorWithTutor.setName("Minor");
        minorWithTutor.setSurname("WithTutor");
        minorWithTutor.setAge(15);
        minorWithTutor.setTutorId(tutor);
        minorWithTutor.setEmail("minorwt@test.com");

        minorWithoutTutor = new User();
        minorWithoutTutor.setId(4L);
        minorWithoutTutor.setName("Minor");
        minorWithoutTutor.setSurname("NoTutor");
        minorWithoutTutor.setAge(16);
        minorWithoutTutor.setEmail("minornt@test.com");

        Travel travel = new Travel();
        travel.setId(1L);

        booking = new Booking();
        booking.setBookingId(1L);
        booking.setBoughtDate(LocalDateTime.now());
        booking.setTypeBoard(TypeBoard.HALF);
        booking.setIsGroup(false);
        booking.setTotalPrice(500.0);
        booking.setTravel(travel);
        booking.setCustomers(new ArrayList<>());
    }

    @Test
    void findAll_ShouldReturnAllBookings() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<Booking> result = bookingService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void findById_ShouldReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<Booking> result = bookingService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getBookingId()).isEqualTo(1L);
    }

    @Test
    void findById_ShouldReturnEmptyOptional() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Booking> result = bookingService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void save_ShouldSaveBookingWithValidCustomers() {
        booking.setCustomers(List.of(adultUser));
        when(userRepository.findById(adultUser.getId())).thenReturn(Optional.of(adultUser));
        when(bookingPricingService.calculateTotalPrice(booking)).thenReturn(500.0);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.save(booking);

        assertThat(result).isNotNull();
        verify(userRepository).findById(adultUser.getId());
        verify(bookingPricingService).calculateTotalPrice(booking);
    }

    @Test
    void save_ShouldThrowMinorWithoutTutorException() {
        booking.setCustomers(List.of(minorWithoutTutor));
        when(userRepository.findById(minorWithoutTutor.getId())).thenReturn(Optional.of(minorWithoutTutor));

        assertThatThrownBy(() -> bookingService.save(booking))
                .isInstanceOf(MinorWithoutTutorException.class);
    }

    @Test
    void save_ShouldThrowIllegalArgumentExceptionWhenCustomerHasNoId() {
        User customerWithoutId = new User();
        customerWithoutId.setName("NoId");
        booking.setCustomers(List.of(customerWithoutId));

        assertThatThrownBy(() -> bookingService.save(booking))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_ShouldUpdateBooking() {
        Booking bookingDetails = new Booking();
        bookingDetails.setBoughtDate(LocalDateTime.now());
        bookingDetails.setTypeBoard(TypeBoard.FULL);
        bookingDetails.setIsGroup(true);
        bookingDetails.setCustomers(List.of(adultUser));
        Travel travel = new Travel();
        travel.setId(2L);
        bookingDetails.setTravel(travel);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(adultUser.getId())).thenReturn(Optional.of(adultUser));
        when(bookingPricingService.calculateTotalPrice(any(Booking.class))).thenReturn(800.0);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.update(1L, bookingDetails);

        assertThat(result).isNotNull();
    }

    @Test
    void update_ShouldThrowResourceNotFoundException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.update(99L, new Booking()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteById_ShouldDeleteBooking() {
        when(bookingRepository.existsById(1L)).thenReturn(true);

        bookingService.deleteById(1L);

        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowResourceNotFoundException() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookingService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addCustomerToBooking_ShouldAddCustomer() {
        BookingUserRequestDTO request = new BookingUserRequestDTO();
        request.setBookingId(1L);
        request.setUserId(2L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(2L)).thenReturn(Optional.of(adultUser));
        when(bookingPricingService.calculateTotalPrice(booking)).thenReturn(600.0);
        when(bookingRepository.save(booking)).thenReturn(booking);

        bookingService.addCustomerToBooking(request);

        assertThat(booking.getCustomers()).contains(adultUser);
    }

    @Test
    void addCustomerToBooking_ShouldThrowWhenBookingNotFound() {
        BookingUserRequestDTO request = new BookingUserRequestDTO();
        request.setBookingId(99L);
        request.setUserId(2L);

        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.addCustomerToBooking(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addCustomerToBooking_ShouldThrowWhenUserNotFound() {
        BookingUserRequestDTO request = new BookingUserRequestDTO();
        request.setBookingId(1L);
        request.setUserId(99L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.addCustomerToBooking(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void addCustomerToBooking_ShouldThrowMinorWithoutTutor() {
        BookingUserRequestDTO request = new BookingUserRequestDTO();
        request.setBookingId(1L);
        request.setUserId(4L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(4L)).thenReturn(Optional.of(minorWithoutTutor));

        assertThatThrownBy(() -> bookingService.addCustomerToBooking(request))
                .isInstanceOf(MinorWithoutTutorException.class);
    }

    @Test
    void quote_ShouldReturnQuote() {
        BookingQuoteRequestDTO quoteRequest = new BookingQuoteRequestDTO();
        BookingQuoteResponseDTO quoteResponse = new BookingQuoteResponseDTO();

        when(bookingPricingService.quote(quoteRequest)).thenReturn(quoteResponse);

        BookingQuoteResponseDTO result = bookingService.quote(quoteRequest);

        assertThat(result).isEqualTo(quoteResponse);
    }
}
