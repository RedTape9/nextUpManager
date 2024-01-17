package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.EmployeeGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    @GetMapping
    public List<EmployeeGetForOptionDTO> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


}
