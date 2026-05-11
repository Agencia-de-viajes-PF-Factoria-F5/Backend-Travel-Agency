package com.inditex.g1_agencia_viajes.repository;

import com.inditex.g1_agencia_viajes.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
