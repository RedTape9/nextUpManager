package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.repo.NextUpCustomerRepository;
import io.github.redtape9.nextupmanager.backend.model.Customers;
import io.github.redtape9.nextupmanager.backend.model.CustomerUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NextUpCustomerService {
    private final NextUpCustomerRepository nextUpCustomerRepository;

    public List<Customers> getAllCustomers() {
        return nextUpCustomerRepository.findAll();
    }

    public Optional<Customers> getCustomerById(String id) {
        return nextUpCustomerRepository.findById(id);
    }

    public Customers createCustomer(Customers customer) {
        return nextUpCustomerRepository.save(customer);
    }

    public Customers updateCustomer(String id, CustomerUpdateDTO updateDTO) {
        Customers customer = nextUpCustomerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateCustomerWithDTO(customer, updateDTO);
        return nextUpCustomerRepository.save(customer);
    }

    private void updateCustomerWithDTO(Customers customer, CustomerUpdateDTO updateDTO) {
        customer.setDepartmentId(updateDTO.getDepartmentId());
        customer.setStatusHistory(updateDTO.getStatusHistory());
        customer.setCurrentStatus(updateDTO.getCurrentStatus());
        customer.setEmployeeId(updateDTO.getEmployeeId());
        customer.setCommentByEmployee(updateDTO.getCommentByEmployee());
    }

    public void deleteCustomer(String id) {
        nextUpCustomerRepository.deleteById(id);
    }

}
