package com.example.montytask.reposetories;

import com.example.montytask.models.entity.Department;
import com.example.montytask.models.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByNameContaining(String name);
    List<Department> findByName(String departmentName);
}
