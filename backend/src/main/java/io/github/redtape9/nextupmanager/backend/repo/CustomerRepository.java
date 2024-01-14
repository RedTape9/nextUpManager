package io.github.redtape9.nextupmanager.backend.repo;


import io.github.redtape9.nextupmanager.backend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByDepartmentId(String departmentId);
}