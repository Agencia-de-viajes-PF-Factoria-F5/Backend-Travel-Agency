package com.inditex.g1_agencia_viajes.service;

import com.inditex.g1_agencia_viajes.exception.ResourceNotFoundException;
import com.inditex.g1_agencia_viajes.model.Employee;
import com.inditex.g1_agencia_viajes.repository.EmployeeRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        String passwordPlain = employee.getPassword();
        String encryptedPassword = BCrypt.hashpw(passwordPlain, BCrypt.gensalt());
        employee.setPassword(encryptedPassword);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee details) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l empleado", id));

        existing.setName(details.getName());
        existing.setSurname(details.getSurname());
        existing.setGender(details.getGender());
        existing.setWorkHour(details.getWorkHour());
        existing.setHired(details.getHired());
        existing.setRole(details.getRole());

        if (details.getPassword() != null && !details.getPassword().isBlank()) {
            existing.setPassword(BCrypt.hashpw(details.getPassword(), BCrypt.gensalt()));
        }

        return employeeRepository.save(existing);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}