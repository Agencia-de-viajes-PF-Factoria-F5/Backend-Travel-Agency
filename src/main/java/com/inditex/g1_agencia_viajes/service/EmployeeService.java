package com.inditex.g1_agencia_viajes.service;

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