package com.example.employeemodel.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.example.employeemodel.validations.ValidateEmployeeStatus;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;

    @NotBlank(message = "name shouldn't be null or empty")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "achievedTarget shouldn't be null")
    @Min(value = 0, message = "achievedTarget shouldn't be less than 0")
    @Column(nullable = false)
    private Integer achievedTarget;

    @ValidateEmployeeStatus
    @Column(nullable = false)
    private String status;

    // Default constructor
    public Employee() {}

    // Parameterized constructor
    public Employee(String name, Integer achievedTarget, String status) {
        this.name = name;
        this.achievedTarget = achievedTarget;
        this.status = status;
    }

    // Getters and setters
    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAchievedTarget() {
        return achievedTarget;
    }

    public void setAchievedTarget(Integer achievedTarget) {
        this.achievedTarget = achievedTarget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}