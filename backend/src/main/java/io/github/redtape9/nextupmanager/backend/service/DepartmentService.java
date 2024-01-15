package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department updatedData) {
        return departmentRepository.findById(updatedData.getId())
                .map(department -> {
                    department.setName(updatedData.getName());
                    department.setPrefix(updatedData.getPrefix());
                    department.setCurrentNumber(updatedData.getCurrentNumber());
                    return departmentRepository.save(department);
                })
                .orElseThrow(() -> new IllegalArgumentException("Department with id: " + updatedData.getId() + " not found"));
    }
}