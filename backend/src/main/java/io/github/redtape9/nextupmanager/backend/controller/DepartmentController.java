package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    // GET
    @GetMapping
    public List<DepartmentGetForOptionDTO> getDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentGetForOptionDTO getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id);
    }


}
