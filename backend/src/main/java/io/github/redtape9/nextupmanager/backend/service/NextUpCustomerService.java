package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.repo.NextUpCustomerRepository;
import io.github.redtape9.nextupmanager.backend.model.Customer;
import io.github.redtape9.nextupmanager.backend.model.CustomerUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NextUpCustomerService {
    private final NextUpCustomerRepository nextUpCustomerRepository;

    public List<Customer> getAllCustomers() {
        return nextUpCustomerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        return nextUpCustomerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return nextUpCustomerRepository.save(customer);
    }

    public Customer updateCustomer(String id, CustomerUpdateDTO updateDTO) {
        Customer customer = nextUpCustomerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateCustomerWithDTO(customer, updateDTO);
        return nextUpCustomerRepository.save(customer);
    }

    private void updateCustomerWithDTO(Customer customer, CustomerUpdateDTO updateDTO) {
        customer.setDepartmentId(updateDTO.getDepartmentId());
        customer.setStatusHistory(updateDTO.getStatusHistory());
        customer.setCurrentStatus(updateDTO.getCurrentStatus());
        customer.setEmployeeId(updateDTO.getEmployeeId());
        customer.setCommentByEmployee(updateDTO.getCommentByEmployee());
    }

    public void deleteCustomer(String id) {
        nextUpCustomerRepository.deleteById(id);
    }

    public List<Customer> getCustomersByDepartment(String departmentId) {
        return nextUpCustomerRepository.findByDepartmentId(departmentId);
    }
}
