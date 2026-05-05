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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customers_bookings", // Nombre de la tabla intermedia en la BD
            joinColumns = @JoinColumn(name = "booking_id"), // FK de esta entidad (Booking)
            inverseJoinColumns = @JoinColumn(name = "costumer_id") // FK de la otra entidad (User)
    )
    private List<User> customers;
}
