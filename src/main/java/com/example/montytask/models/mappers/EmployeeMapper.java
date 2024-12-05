package com.example.montytask.models.mappers;

import com.example.montytask.models.DTO.EmployeeDto;
import com.example.montytask.models.entity.Department;
import com.example.montytask.models.entity.Employee;
import com.example.montytask.reposetories.DepartmentRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapper {
    private final DepartmentRepository departmentRepository;

    public EmployeeMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public EmployeeDto toDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDepartmentId(employee.getDepartment().getId().toString());
        dto.setDepartmentName(employee.getDepartment().getName());
        dto.setSkills(employee.getSkills());
        return dto;
    }

    public Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setSkills(dto.getSkills());
        if (dto.getDepartmentId() != null) {
            Long departmentId = Long.parseLong(dto.getDepartmentId());
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid department ID: " + departmentId));
            employee.setDepartment(department);
        }
        return employee;
    }
}
