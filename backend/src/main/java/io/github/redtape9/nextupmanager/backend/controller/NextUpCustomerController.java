package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Customer;
import io.github.redtape9.nextupmanager.backend.model.CustomerUpdateDTO;
import io.github.redtape9.nextupmanager.backend.service.NextUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class NextUpCustomerController {
    private final NextUpCustomerService customerService;


    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if(customer.getDepartmentId() == null) {
            throw new IllegalArgumentException("DepartmentId darf nicht null sein");
        }
        if(isValidCustomer(customer)) {
            Customer createdCustomer = customerService.createCustomer(customer);
            return ResponseEntity.ok(createdCustomer);
        } else {
            throw new IllegalArgumentException("Kundeneingaben sind nicht valide");
        }

    }

    private boolean isValidCustomer(Customer customer) {
        return customer.getDepartmentId() != null && customer.getCurrentStatus() != null;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
    }

    @GetMapping("/department/{departmentId}")
    public List<Customer> getCustomersByDepartment(@PathVariable String departmentId) {
        return customerService.getCustomersByDepartment(departmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String id, @RequestBody CustomerUpdateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            Customer updatedCustomer = customerService.updateCustomer(id, updateDTO);
            return ResponseEntity.ok(updatedCustomer);
        } else {
            throw new IllegalArgumentException("Kundeneingaben sind nicht valide");
        }
    }

    private boolean isValidCustomerUpdateDTO(CustomerUpdateDTO updateDTO) {
        return updateDTO.getCurrentStatus() != null;
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        Optional<Customer> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isPresent()) {
            customerService.deleteCustomer(id);
        } else {
            throw new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden");
        }
    }


}
