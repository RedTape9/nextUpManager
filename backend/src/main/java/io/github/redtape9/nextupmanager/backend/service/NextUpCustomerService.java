package io.github.redtape9.nextupmanager.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.redtape9.nextupmanager.backend.repo.NextUpCustomerRepository;
import io.github.redtape9.nextupmanager.backend.model.NextUpCustomer;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NextUpCustomerService {
    private final NextUpCustomerRepository nextUpCustomerRepository;

    public List<NextUpCustomer> getAllCustomers() {
        return nextUpCustomerRepository.findAll();
    }

    public Optional<NextUpCustomer> getCustomerById(String id) {
        return nextUpCustomerRepository.findById(id);
    }
    public NextUpCustomer createOrUpdateCustomer(NextUpCustomer customer) {
        return nextUpCustomerRepository.save(customer);
    }
    public void deleteCustomer(String id) {
        nextUpCustomerRepository.deleteById(id);
    }

}
