
package com.example.montytask;

import com.example.montytask.models.entity.Department;
import com.example.montytask.models.entity.Employee;
import com.example.montytask.reposetories.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
//        Authentication auth = new UsernamePasswordAuthenticationToken(
//                "mockAdmin", null, List.of(new SimpleGrantedAuthority("ADMIN"))
//        );
//        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        String payload = """
                {
                    "name": "Hossam",
                    "departmentId": "1",
                    "skills": "Java,Spring"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hossam"));
    }

    @Test
    void shouldReturnEmployeesBasedOnSearch() throws Exception {
        Employee employee = new Employee();
        employee.setName("hossam");
        employee.setDepartment(Department.builder().id(1L).build());
        employeeRepository.save(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/search")
                        .param("name", "hossam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("hossam"));
    }


    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}
