package io.github.redtape9.nextupmanager.backend;

import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForDetailsDTO;
import io.github.redtape9.nextupmanager.backend.entity.Employee;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    Employee employee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee();
        employee.setId("1");
        employee.setName("Franz");
        employee.setSurname("Broetchen");
        employee.setDepartmentId("Kueche");
        employee.setRoom("K01");
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenExists() {
        // Given
        String employeeId = "1";
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        EmployeeGetForDetailsDTO result = employeeService.getEmployeeById(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        assertEquals(employee.getName(), result.getName());
        assertEquals(employee.getSurname(), result.getSurname());
        assertEquals(employee.getDepartmentId(), result.getDepartmentId());
        assertEquals(employee.getRoom(), result.getRoom());
    }


}


