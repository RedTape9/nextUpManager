package io.github.redtape9.nextupmanager.backend;

import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForDetailsDTO;
import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.entity.Employee;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    Employee employee;
    Employee employee2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        employee = createEmployee("1", "Franzi", "Broetchen", "Kueche", "K01");
        employee2 = createEmployee("2", "Hans", "Wurst", "Kueche", "K01");
    }

    private Employee createEmployee(String id, String name, String surname, String departmentId, String room) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setDepartmentId(departmentId);
        employee.setRoom(room);
        return employee;
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

    @Test
    void getAllEmployees_ShouldReturnAllEmployees() {
        // Given
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        employees.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<EmployeeGetForOptionDTO> result = employeeService.getAllEmployees();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(employee.getId(), result.get(0).getId());
        assertEquals(employee.getName(), result.get(0).getName());
        assertEquals(employee.getSurname(), result.get(0).getSurname());
        assertEquals(employee.getRoom(), result.get(0).getRoom());
        assertEquals(employee2.getId(), result.get(1).getId());
        assertEquals(employee2.getName(), result.get(1).getName());
        assertEquals(employee2.getSurname(), result.get(1).getSurname());
        assertEquals(employee2.getRoom(), result.get(1).getRoom());


    }
}



