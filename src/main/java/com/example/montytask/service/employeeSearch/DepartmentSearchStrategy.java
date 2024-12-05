package com.example.montytask.service.employeeSearch;

import com.example.montytask.models.entity.Employee;

public class DepartmentSearchStrategy implements EmployeeSearchStrategy {
    private final String department;

    public DepartmentSearchStrategy(String department) {
        this.department = department;
    }

    @Override
    public boolean matches(Employee employee) {
        return department == null || (employee.getDepartment() != null 
                && employee.getDepartment().getName().toLowerCase().contains(department.toLowerCase()));
    }
}
