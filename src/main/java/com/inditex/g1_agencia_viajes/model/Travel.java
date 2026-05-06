package com.inditex.g1_agencia_viajes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El destino es obligatorio")
    private String destiny;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate endDate;

    private Boolean sale = false;

    @Min(value = 0, message = "Las plazas no pueden ser negativas")
    private Integer availablePlaces;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<TripSegment> tripSegments;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id") // Crea físicamente la columna FK "offer_id" en la tabla travels
    private Offer offer;
}