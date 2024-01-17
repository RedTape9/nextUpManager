package io.github.redtape9.nextupmanager.backend.repo;

import io.github.redtape9.nextupmanager.backend.model.Department;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, String> {
    Department findByName(String name);

}