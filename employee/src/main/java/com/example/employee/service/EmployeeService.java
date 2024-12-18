package com.example.employee.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Save Employee Details
    public Employee saveEmployee(Employee employee) {
        if (employee.getFirstName() == null || employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Employee data is invalid.");
        }
        return employeeRepository.save(employee);
    }

    // Delete Employee By Id
    public String deleteEmployeeById(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new NoSuchElementException("Employee not found for the given ID: " + id);
        }
        employeeRepository.deleteById(id);
        return "The requested id got deleted";
    }
}
