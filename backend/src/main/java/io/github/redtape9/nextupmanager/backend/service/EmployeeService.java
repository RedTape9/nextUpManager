package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Employee;
import io.github.redtape9.nextupmanager.backend.model.EmployeeGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public List<EmployeeGetForOptionDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> {
                    EmployeeGetForOptionDTO employeeGetForOptionDTO = new EmployeeGetForOptionDTO();
                    employeeGetForOptionDTO.setId(employee.getId());
                    employeeGetForOptionDTO.setName(employee.getName());
                    employeeGetForOptionDTO.setSurname(employee.getSurname());
                    return employeeGetForOptionDTO;
                })
                .toList();
    }
}