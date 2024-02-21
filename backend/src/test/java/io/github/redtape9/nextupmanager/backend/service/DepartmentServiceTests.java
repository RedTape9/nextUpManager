package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.entity.Department;
import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
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

public class DepartmentServiceTests {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private Department department;
    private Department department2;
    private List<Department> departments;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        department = new Department();
        department.setId("666");
        department.setName("IT");
        department.setPrefix("IT-");
        department.setCurrentNumber(101);

        department2 = new Department();
        department2.setId("667");
        department2.setName("HR");
        department2.setPrefix("HR-");
        department2.setCurrentNumber(102);

        departments = new ArrayList<>();
        departments.add(department);
        departments.add(department2);
    }

    @Test
    void updateDepartment_ShouldReturnUpdatedDepartment() {
        // Given
        when(departmentRepository.save(department)).thenReturn(department);

        // When
        Department result = departmentService.updateDepartment(department);

        // Then
        assertNotNull(result);
        assertEquals(department.getId(), result.getId());
        assertEquals(department.getName(), result.getName());
        assertEquals(department.getPrefix(), result.getPrefix());
        assertEquals(department.getCurrentNumber(), result.getCurrentNumber());
    }

    @Test
    void getAllDepartments_ShouldReturnAllDepartments() {
        // Given
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        List<DepartmentGetForOptionDTO> result = departmentService.getAllDepartments();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(department.getId(), result.get(0).getId());
        assertEquals(department.getName(), result.get(0).getName());
        assertEquals(department2.getId(), result.get(1).getId());
        assertEquals(department2.getName(), result.get(1).getName());
    }

    @Test
    void getDepartmentById_ShouldReturnDepartment_WhenExists() {
        // Given
        String departmentId = "666";
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        // When
        DepartmentGetForOptionDTO result = departmentService.getDepartmentById(departmentId);

        // Then
        assertNotNull(result);
        assertEquals(department.getId(), result.getId());
        assertEquals(department.getName(), result.getName());
    }
}