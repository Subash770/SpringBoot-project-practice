package com.example.employeemodel;

import com.example.employeemodel.entity.Employee;
import com.example.employeemodel.repository.EmployeeRepository;
import com.example.employeemodel.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeServiceTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void testAddEmployee() throws Exception {
        Employee employee = new Employee("John Doe", 300,"Not Achieved");
        
        Employee getEmployee = employeeService.saveEmployee(employee);

        assertEquals(employee.getEmployeeId(), getEmployee.getEmployeeId());
        assertEquals(employee.getName(), getEmployee.getName());
        assertEquals(employee.getAchievedTarget(), getEmployee.getAchievedTarget());
        assertEquals(employee.getStatus(), getEmployee.getStatus());
    }


    @Test
    public void testGetAllExpectedTarget() throws Exception {
        Employee employee1 = new Employee("John Doe", 600,"Achieved");
        Employee employee2 = new Employee("Melvin Doe", 300,"Not Achieved");
        Employee employee3 = new Employee("Yuvan K", 800,"Achieved");

        employeeRepository.saveAll(Arrays.asList(employee1, employee2, employee3));

        List<Employee> getEmployees = employeeService.getAllExpectedTarget(500);
        
        assertEquals(2, getEmployees.size());
        assertEquals(employee1.getAchievedTarget(), getEmployees.get(0).getAchievedTarget());
        assertEquals(employee3.getAchievedTarget(), getEmployees.get(1).getAchievedTarget());
    }

}
