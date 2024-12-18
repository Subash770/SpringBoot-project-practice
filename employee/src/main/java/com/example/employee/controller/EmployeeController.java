package com.example.employee.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.employee.entity.Employee;
import com.example.employee.service.EmployeeService;

@RestController
@CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public ResponseEntity<String> getMessage() {
        return new ResponseEntity<>("Hello World!!!", HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> listEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return allEmployees.isEmpty()
            ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
            : new ResponseEntity<>(allEmployees, HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> deleteEmployee(@PathVariable Integer employeeId) {
        try {
            String response = employeeService.deleteEmployeeById(employeeId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Employee not found for the given ID: " + employeeId, HttpStatus.NOT_FOUND);
        }
    }

}
