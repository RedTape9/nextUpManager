package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketAssigmentDTO;
import io.github.redtape9.nextupmanager.backend.model.TicketCreateDTO;
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

    // GET for select ticket in frontend by department name

    // UPDATE for assigment
    @PutMapping("/next/{employeeId}")
    public TicketAssigmentDTO assignNextTicketToEmployee(@PathVariable String employeeId) {
        return ticketService.assignNextTicket(employeeId);
    }


    // UPDATE for simple version
    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable String id, @RequestBody TicketCreateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            return ticketService.updateTicket(id, updateDTO);
        } else {
            throw new IllegalArgumentException("Ticketeingaben sind nicht valide");
        }
    }

    private boolean isValidCustomerUpdateDTO(TicketCreateDTO updateDTO) {
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
