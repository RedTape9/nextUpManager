package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.entity.Department;
import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.dto.DepartmentUpdateNumberDTO;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;


    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<DepartmentGetForOptionDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(department -> {
                    DepartmentGetForOptionDTO departmentGetForOptionDTO = new DepartmentGetForOptionDTO();
                    departmentGetForOptionDTO.setId(department.getId());
                    departmentGetForOptionDTO.setName(department.getName());
                    return departmentGetForOptionDTO;
                })
                .toList();
    }

    public DepartmentGetForOptionDTO getDepartmentById(String id) {
        Department department = departmentRepository.findById(id).orElse(null);
        if (department == null) {
            throw new IllegalArgumentException("Department mit id: " + id + " existiert nicht");
        }
        DepartmentGetForOptionDTO departmentGetForOptionDTO = new DepartmentGetForOptionDTO();
        departmentGetForOptionDTO.setId(department.getId());
        departmentGetForOptionDTO.setName(department.getName());
        return departmentGetForOptionDTO;
    }
}




