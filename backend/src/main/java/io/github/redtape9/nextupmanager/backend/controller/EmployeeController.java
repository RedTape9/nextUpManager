package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForDetailsDTO;
import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @GetMapping
    public List<EmployeeGetForOptionDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeGetForDetailsDTO getEmployeeById(@PathVariable String id) {
        return employeeService.getEmployeeById(id);
    }

}
