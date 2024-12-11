package com.example.employeemodel.validations;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmployeeValidator implements ConstraintValidator<ValidateEmployeeStatus, String> {

    @Override
    public boolean isValid(String status, ConstraintValidatorContext constraintValidatorContext) {
        List<String> employeeStatus = Arrays.asList("Achieved", "Not Achieved");
        return employeeStatus.contains(status);
    }
}
