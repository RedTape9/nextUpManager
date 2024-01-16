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

    public Department updateDepartmentByName(String name, Department updatedDepartmentData) {
        Department existingDepartment = departmentRepository.findByName(name);
        if (existingDepartment == null) {
            throw new IllegalArgumentException("Department mit dem Namen: " + name + " nicht gefunden");
        }

        // Aktualisieren der vorhandenen Department-Daten mit den neuen Daten
        updateDepartmentData(existingDepartment, updatedDepartmentData);

        // Speichern des aktualisierten Departments
        return departmentRepository.save(existingDepartment);
    }

    private void updateDepartmentData(Department existingDepartment, Department updatedDepartmentData) {
        // Hier übernehmen Sie die relevanten Daten von updatedDepartmentData
        // Beispiel: existingDepartment.setPrefix(updatedDepartmentData.getPrefix());
        // Fügen Sie hier weitere Feldaktualisierungen hinzu
        existingDepartment.setCurrentNumber(updatedDepartmentData.getCurrentNumber());
        // Fügen Sie zusätzliche Aktualisierungen basierend auf Ihren Anforderungen hinzu
    }

    /*    public Department getDepartmentById(String id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Abteilung mit der id: " + id + " nicht gefunden"));
    }*/
}




