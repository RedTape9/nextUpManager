package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.model.DepartmentGetDTO;
import io.github.redtape9.nextupmanager.backend.model.DepartmentUpdateDTO;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department department) {
        DepartmentUpdateDTO updateDTO = new DepartmentUpdateDTO();
        updateDTO.setCurrentNumber(department.getCurrentNumber());
        return applyDepartmentUpdate(department, updateDTO);
    }

    private Department applyDepartmentUpdate(Department existingDepartment, DepartmentUpdateDTO updateDTO) {
        existingDepartment.setCurrentNumber(updateDTO.getCurrentNumber());
        // Weitere Aktualisierungen hier
        return departmentRepository.save(existingDepartment);
    }

       public Department getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Abteilung mit der id: " + id + " nicht gefunden"));
    }

    public List<DepartmentGetDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> {
                    DepartmentGetDTO departmentGetDTO = new DepartmentGetDTO();
                    departmentGetDTO.setId(department.getId());
                    departmentGetDTO.setName(department.getName());
                    departmentGetDTO.setPrefix(department.getPrefix());
                    departmentGetDTO.setCurrentNumber(department.getCurrentNumber());
                    return departmentGetDTO;
                })
                .toList();
    }
}




