package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.NextUpCustomer;
import io.github.redtape9.nextupmanager.backend.service.NextUpCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class NextUpCustomerController {
    private final NextUpCustomerService customerService;

    @PostMapping
    public NextUpCustomer createCustomer(@RequestBody NextUpCustomer customer) {
        return customerService.createOrUpdateCustomer(customer);

    }



    @GetMapping
    public List<NextUpCustomer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public NextUpCustomer getCustomerById(@PathVariable String id) {
        return customerService.getCustomerById(id).orElseThrow();
    }




}
