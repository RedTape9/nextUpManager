package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForDetailsDTO;
import io.github.redtape9.nextupmanager.backend.entity.Employee;
import io.github.redtape9.nextupmanager.backend.dto.EmployeeGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeGetForDetailsDTO getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mitarbeiter nicht gefunden");
        }
        EmployeeGetForDetailsDTO employeeGetForDetailsDTO = new EmployeeGetForDetailsDTO();
        employeeGetForDetailsDTO.setId(employee.getId());
        employeeGetForDetailsDTO.setName(employee.getName());
        employeeGetForDetailsDTO.setSurname(employee.getSurname());
        employeeGetForDetailsDTO.setDepartmentId(employee.getDepartmentId());
        employeeGetForDetailsDTO.setRoom(employee.getRoom());
        return employeeGetForDetailsDTO;
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