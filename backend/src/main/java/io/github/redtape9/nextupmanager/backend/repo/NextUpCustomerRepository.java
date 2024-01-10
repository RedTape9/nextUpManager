package io.github.redtape9.nextupmanager.backend.repo;


import io.github.redtape9.nextupmanager.backend.model.NextUpCustomer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NextUpCustomerRepository extends MongoRepository<NextUpCustomer, String> {
}