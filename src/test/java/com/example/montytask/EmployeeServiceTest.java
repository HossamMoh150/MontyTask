package com.example.montytask;

import com.example.montytask.models.DTO.EmployeeDto;
import com.example.montytask.models.entity.Employee;
import com.example.montytask.models.mappers.EmployeeMapper;
import com.example.montytask.reposetories.DepartmentRepository;
import com.example.montytask.reposetories.EmployeeRepository;
import com.example.montytask.service.EmployeeService;
import org.junit.jupiter.api.extension.ExtendWith;
import com.example.montytask.models.DTO.AuthResponse;
import com.example.montytask.models.DTO.RegisterRequest;
import com.example.montytask.models.entity.AppUser;
import com.example.montytask.reposetories.UserRepository;
import com.example.montytask.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void shouldSaveEmployee() {
        EmployeeDto dto =  EmployeeDto.builder().name("Hossam").departmentId("1").skills("Java,Spring").build();

        Employee employee = new Employee();

        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDto(employee)).thenReturn(dto);

        EmployeeDto savedDto = employeeService.save(dto);

        assertEquals(dto.getName(), savedDto.getName());
    }

    @Test
    void shouldSearchEmployeesByName() {
        Employee employee = new Employee();
        employee.setName("Hossam");

        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        List<EmployeeDto> result = employeeService.searchEmployees("Hossam", null, null);

        assertFalse(result.isEmpty());
    }
}
