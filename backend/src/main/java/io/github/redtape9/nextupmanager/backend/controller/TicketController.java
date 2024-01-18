package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.TicketUpdateDTO;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.dto.TicketAssigmentDTO;
import io.github.redtape9.nextupmanager.backend.dto.TicketCreateDTO;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
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

    // Getter auf DTOS umstellen
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
    /*@PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable String id, @RequestBody TicketCreateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            return ticketService.updateTicket(id, updateDTO);
        } else {
            throw new IllegalArgumentException("Ticketeingaben sind nicht valide");
        }
    }*/

    // UPDATE for status change to FINISHED or CANCELED

    @PutMapping("/{id}/status")
    public Ticket updateTicketStatus(
            @PathVariable String id,
            @RequestBody TicketUpdateDTO updateDTO) {

        return ticketService.updateTicketStatus(id, updateDTO);
    }

    private boolean isValidCustomerUpdateDTO(TicketCreateDTO updateDTO) {
        return updateDTO.getCurrentStatus() != null;
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable String id) {
        Optional<Ticket> customerOptional = ticketService.getTicketById(id);
        if (customerOptional.isPresent()) {
            ticketService.deleteTicket(id);
        } else {
            throw new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden");
        }
    }


}
