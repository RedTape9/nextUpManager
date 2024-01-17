package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.model.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.model.DepartmentUpdateNumberDTO;
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
        DepartmentUpdateNumberDTO updateDTO = new DepartmentUpdateNumberDTO();
        updateDTO.setCurrentNumber(department.getCurrentNumber());
        return applyDepartmentUpdate(department, updateDTO);
    }

    private Department applyDepartmentUpdate(Department existingDepartment, DepartmentUpdateNumberDTO updateDTO) {
        existingDepartment.setCurrentNumber(updateDTO.getCurrentNumber());
        // Weitere Aktualisierungen hier
        return departmentRepository.save(existingDepartment);
    }

       public Department getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Abteilung mit der id: " + id + " nicht gefunden"));
    }

    public List<DepartmentGetForOptionDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> {
                    DepartmentGetForOptionDTO departmentGetForOptionDTO = new DepartmentGetForOptionDTO();
                    departmentGetForOptionDTO.setId(department.getId());
                    departmentGetForOptionDTO.setName(department.getName());
                    departmentGetForOptionDTO.setPrefix(department.getPrefix());
                    departmentGetForOptionDTO.setCurrentNumber(department.getCurrentNumber());
                    return departmentGetForOptionDTO;
                })
                .toList();
    }
}




