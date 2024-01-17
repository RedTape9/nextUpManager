package io.github.redtape9.nextupmanager.backend.repo;

import io.github.redtape9.nextupmanager.backend.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {

}