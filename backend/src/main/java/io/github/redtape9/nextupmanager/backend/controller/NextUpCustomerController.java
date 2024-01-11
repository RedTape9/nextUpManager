package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Customers;
import io.github.redtape9.nextupmanager.backend.model.CustomerUpdateDTO;
import io.github.redtape9.nextupmanager.backend.service.NextUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class NextUpCustomerController {
    private final NextUpCustomerService customerService;


    @PostMapping
    public Customers createCustomer(@RequestBody Customers customer) {
        return customerService.createCustomer(customer);

    }

    @GetMapping
    public List<Customers> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customers getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
    }

    @PutMapping("/{id}")
    public Customers updateCustomer(@PathVariable String id, @RequestBody CustomerUpdateDTO updateDTO) {
        return customerService.updateCustomer(id, updateDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        Optional<Customers> customerOptional = customerService.getCustomerById(id);
        if (customerOptional.isPresent()) {
            customerService.deleteCustomer(id);
        } else {
            throw new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden");
        }
    }


}
