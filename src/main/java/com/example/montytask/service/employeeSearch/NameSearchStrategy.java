package com.example.montytask.service.employeeSearch;

import com.example.montytask.models.entity.Employee;

public class NameSearchStrategy implements EmployeeSearchStrategy {
    private final String name;

    public NameSearchStrategy(String name) {
        this.name = name;
    }

    @Override
    public boolean matches(Employee employee) {
        return name == null || employee.getName().toLowerCase().contains(name.toLowerCase());
    }
}
