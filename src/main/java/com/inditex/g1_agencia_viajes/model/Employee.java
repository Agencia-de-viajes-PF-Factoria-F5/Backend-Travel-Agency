package com.inditex.g1_agencia_viajes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Column(name = "surname")
    private String surname;

    @NotNull(message = "El género es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "work_hour")
    private Integer workHour;

    @NotNull(message = "El estado de contratación es obligatorio")
    private Boolean hired;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "employee")
    private List<Booking> bookings;
}