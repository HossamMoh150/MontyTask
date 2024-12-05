package com.example.montytask.service.employeeSearch;

import com.example.montytask.models.entity.Employee;

import java.util.List;

public class SkillsSearchStrategy implements EmployeeSearchStrategy {
    private final List<String> skills;

    public SkillsSearchStrategy(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public boolean matches(Employee employee) {
        return skills == null || skills.stream().allMatch(skill ->
                employee.getSkills() != null && employee.getSkills().toLowerCase().contains(skill.toLowerCase()));
    }
}
