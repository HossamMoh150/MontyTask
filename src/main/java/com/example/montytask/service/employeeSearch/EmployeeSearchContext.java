package com.example.montytask.service.employeeSearch;

import com.example.montytask.models.entity.Employee;

import java.util.List;

public class EmployeeSearchContext {
    private final List<EmployeeSearchStrategy> strategies;

    public EmployeeSearchContext(List<EmployeeSearchStrategy> strategies) {
        this.strategies = strategies;
    }

    public boolean matches(Employee employee) {
        return strategies.stream().allMatch(strategy -> strategy.matches(employee));
    }
}
