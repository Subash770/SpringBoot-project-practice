package com.example.employeemodel.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EmployeeValidator.class)
public @interface ValidateEmployeeStatus {

    public String message() default "Invalid employee status: It should be either Achieved or Not Achieved";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
