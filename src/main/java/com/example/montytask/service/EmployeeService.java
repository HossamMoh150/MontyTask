package com.example.montytask.service;

import com.example.montytask.models.DTO.EmployeeDto;
import com.example.montytask.models.entity.Department;
import com.example.montytask.models.entity.Employee;
import com.example.montytask.models.mappers.EmployeeMapper;
import com.example.montytask.reposetories.DepartmentRepository;
import com.example.montytask.reposetories.EmployeeRepository;
import com.example.montytask.service.employeeSearch.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDto save(EmployeeDto employeeDto) {

        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    public EmployeeDto update(EmployeeDto employeeDto) {

        Employee existingEmployee = findById(employeeDto.getId());
        if (existingEmployee == null) {
            throw new RuntimeException();
        }
        Employee employee = employeeMapper.toEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(savedEmployee);
    }
    @CacheEvict(value = "employees", allEntries = true)
    public EmployeeDto partialUpdate(Long id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        // Update only non-null fields
        if (employeeDto.getName() != null) {
            existingEmployee.setName(employeeDto.getName());
        }
        if (employeeDto.getSkills() != null) {
            existingEmployee.setSkills(employeeDto.getSkills());
        }
        if (employeeDto.getDepartmentId() != null) {
            Long departmentId = Long.parseLong(employeeDto.getDepartmentId());
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid department ID: " + departmentId));
            existingEmployee.setDepartment(department);
        }

        Employee savedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Cacheable(value = "employees")
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    @CacheEvict(value = "employees", allEntries = true)
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<EmployeeDto> searchEmployees(String name, String department, List<String> skills) {
        List<EmployeeSearchStrategy> strategies = List.of(
                new NameSearchStrategy(name),
                new DepartmentSearchStrategy(department),
                new SkillsSearchStrategy(skills)
        );
        EmployeeSearchContext searchContext = new EmployeeSearchContext(strategies);
        List<Employee> allEmployees = employeeRepository.findAll();
        return allEmployees.stream()
                .filter(searchContext::matches)
                .sorted(Comparator.comparing(Employee::getName)) // Optional: sort by name
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
}
