package com.inditex.g1_agencia_viajes.mapper;

import com.inditex.g1_agencia_viajes.dto.BookingResponseDTO;
import com.inditex.g1_agencia_viajes.model.Booking;
import com.inditex.g1_agencia_viajes.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public BookingResponseDTO toDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setBoughtDate(booking.getBoughtDate());
        dto.setTypeBoard(booking.getTypeBoard() != null ? booking.getTypeBoard().name() : null);
        dto.setIsGroup(booking.getIsGroup());
        dto.setTotalPrice(booking.getTotalPrice());

        if (booking.getTravel() != null) {
            dto.setTravelId(booking.getTravel().getId());
            dto.setTravelDestiny(booking.getTravel().getDestiny());
        }

        if (booking.getCustomers() != null) {
            dto.setCustomerIds(booking.getCustomers().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        }

        if (booking.getEmployee() != null) {
            dto.setEmployeeId(booking.getEmployee().getEmployeeId());
        }

        return dto;
    }

    public List<BookingResponseDTO> toDTOList(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
