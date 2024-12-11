package com.example.employeemodel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.employeemodel.entity.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Custom query method to find employees with achievedTarget >= expectedTarget
    List<Employee> findByAchievedTargetGreaterThanEqual(Integer expectedTarget);
}