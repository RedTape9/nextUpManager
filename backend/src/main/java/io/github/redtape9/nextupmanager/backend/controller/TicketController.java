package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketUpdateDTO;
import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final DepartmentService departmentService;



    // CREATE
    @PostMapping("/department/{name}")
    public Ticket createTicket(@RequestBody Ticket ticket, @PathVariable String name) {
        return ticketService.createTicketWithDepartment(ticket, name);
    }

    // GET
    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    // GET for select ticket in frontend
    @GetMapping("department/{name}")
    public List<Ticket> getCustomersByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }
        return ticketService.getAllTicketsByDepartmentId(department.getId());
    }

    @GetMapping("/department/{departmentId}")
    public List<Ticket> getTicketsByDepartment(@PathVariable String departmentId) {
        return ticketService.getAllTicketsByDepartmentId(departmentId);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Ticket updateCustomer(@PathVariable String id, @RequestBody TicketUpdateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            return ticketService.updateTicket(id, updateDTO);
        } else {
            throw new IllegalArgumentException("Ticketeingaben sind nicht valide");
        }
    }

    private boolean isValidCustomerUpdateDTO(TicketUpdateDTO updateDTO) {
        return updateDTO.getCurrentStatus() != null;
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        Optional<Ticket> customerOptional = ticketService.getTicketById(id);
        if (customerOptional.isPresent()) {
            ticketService.deleteCustomer(id);
        } else {
            throw new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden");
        }
    }


}
