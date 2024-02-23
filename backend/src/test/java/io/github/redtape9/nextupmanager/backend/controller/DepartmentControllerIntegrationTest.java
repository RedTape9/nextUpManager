package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerIntegrationTest {

    @MockBean
    private DepartmentService departmentService;

    @Test
    void getAllDepartmentsTest() {
        // GIVEN
        DepartmentGetForOptionDTO department = new DepartmentGetForOptionDTO();
        department.setId("666");
        department.setName("IT");
        List<DepartmentGetForOptionDTO> expected = List.of(department);
        when(departmentService.getAllDepartments()).thenReturn(expected);

        // WHEN
        List<DepartmentGetForOptionDTO> actual = departmentService.getAllDepartments();

        // THEN
        verify(departmentService).getAllDepartments();
        Assertions.assertEquals(expected, actual);
    }
}