package com.inditex.g1_agencia_viajes.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "customers_id")
    private Long customersId;

    @Column(name = "bought_data")
    private LocalDateTime boughtDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_board")
    private TypeBoard typeBoard;

    private Boolean group;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travels_id")
    private Travel travel;
}
