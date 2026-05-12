package com.inditex.g1_agencia_viajes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "bought_date")
    private LocalDateTime boughtDate;

    @NotNull(message = "El tipo de pensión es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_board")
    private TypeBoard typeBoard;

    @Column(name = "is_group")
    private Boolean isGroup;

    @NotNull(message = "El precio total es obligatorio")
    @Column(name = "total_price")
    private Double totalPrice;

    @NotNull(message = "El viaje es obligatorio")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travels_id")
    private Travel travel;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customers_bookings",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private List<User> customers = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}