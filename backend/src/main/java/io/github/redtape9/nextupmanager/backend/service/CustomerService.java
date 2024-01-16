package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.repo.CustomerRepository;
import io.github.redtape9.nextupmanager.backend.model.Customer;
import io.github.redtape9.nextupmanager.backend.model.NewCustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository nextUpCustomerRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<Customer> getAllCustomers() {
        return nextUpCustomerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(String id) {
        return nextUpCustomerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        return nextUpCustomerRepository.save(customer);
    }

    public Customer updateCustomer(String id, NewCustomerDTO updateDTO) {
        Customer customer = nextUpCustomerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateCustomerWithDTO(customer, updateDTO);
        return nextUpCustomerRepository.save(customer);
    }

    private void updateCustomerWithDTO(Customer customer, NewCustomerDTO updateDTO) {
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


    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }


}
