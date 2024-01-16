package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Customer;
import io.github.redtape9.nextupmanager.backend.model.CustomerUpdateDTO;
import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.model.CustomerStatus;
import io.github.redtape9.nextupmanager.backend.service.CustomerService;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final DepartmentService departmentService;




// ... (andere Methoden und Hilfsfunktionen) ...

    @PostMapping("/department/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@RequestBody Customer customer, @PathVariable String name) {
        // Abteilung basierend auf dem Namen holen
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }

        // Kundeninformationen setzen
        customer.setDepartmentId(department.getId());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCurrentStatus(CustomerStatus.WAITING);
        customer.setCustomerNr(department.getPrefix() + (department.getCurrentNumber() + 1));

        // Eintrag in statusHistory hinzufügen
        Customer.StatusChange statusChange = new Customer.StatusChange();
        statusChange.setStatus(CustomerStatus.WAITING);
        statusChange.setTimestamp(customer.getCreatedAt());
        customer.getStatusHistory().add(statusChange);

        // Neuen Kunden in der Datenbank speichern
        Customer createdCustomer = customerService.createCustomer(customer);

        // currentNumber in der Abteilung aktualisieren
        department.setCurrentNumber(department.getCurrentNumber() + 1);
        departmentService.updateDepartmentByName(department.getName(), department);

        return createdCustomer;
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("department/{name}")
    public List<Customer> getCustomersByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }
        return customerService.getCustomersByDepartment(department.getId());
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
