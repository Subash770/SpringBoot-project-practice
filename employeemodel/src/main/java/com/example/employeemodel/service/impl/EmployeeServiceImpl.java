package com.example.employeemodel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employeemodel.entity.Employee;
import com.example.employeemodel.repository.EmployeeRepository;
import com.example.employeemodel.service.EmployeeService;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        // Save the employee to the database
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllExpectedTarget(int expectedTarget) {
        // Retrieve employees whose achievedTarget is greater than or equal to expectedTarget
        return employeeRepository.findByAchievedTargetGreaterThanEqual(expectedTarget);
    }
}