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

    public Department updateDepartment(String id,Department updatedData) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Abteilung mit der id: " + id + " nicht gefunden"));
        updateDepartmentWithDTO(department, updatedData);
        return departmentRepository.save(department);
    }

    private void updateDepartmentWithDTO(Department existingDepartment, Department updatedData) {
        existingDepartment.setName(updatedData.getName());
        existingDepartment.setPrefix(updatedData.getPrefix());
        existingDepartment.setCurrentNumber(updatedData.getCurrentNumber());
    }


}