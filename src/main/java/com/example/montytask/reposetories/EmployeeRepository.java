package com.example.montytask.reposetories;

import com.example.montytask.models.entity.Employee;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContaining(String name);
    List<Employee> findByDepartmentName(String departmentName);
    List<Employee> findBySkillsContaining(String skills);
}
