package com.example.employeemodel.service;

import java.util.List;

import com.example.employeemodel.entity.Employee;

public interface EmployeeService {

    Employee saveEmployee(Employee employee);
    List<Employee> getAllExpectedTarget(int expectedTarget);
  
}
