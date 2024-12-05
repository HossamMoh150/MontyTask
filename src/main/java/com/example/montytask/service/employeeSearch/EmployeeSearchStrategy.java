package com.example.montytask.service.employeeSearch;

import com.example.montytask.models.entity.Employee;

public interface EmployeeSearchStrategy {
    boolean matches(Employee employee);
}
