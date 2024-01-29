package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.*;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    // CREATE
    @PostMapping("/department/{name}")
    public Ticket createTicket(@RequestBody Ticket ticket, @PathVariable String name) {
        return ticketService.createTicketWithDepartment(ticket, name);
    }

    // GET by id
    /*@GetMapping("/{id}")
    public TicketGetByIdDTO getTicketById(@PathVariable String id) {
        Optional<TicketGetByIdDTO> ticketOptional = ticketService.getTicketById(id);
        if (ticketOptional.isPresent()) {
            return ticketOptional.get();
        } else {
            throw new IllegalArgumentException("Ticket mit der id: " + id + " nicht gefunden");
        }
    }*/


    @GetMapping("/waiting")
    public List<TicketGetAllDTO> getAllWaitingTickets() {
        return ticketService.getAllWaitingTickets();
    }

    @GetMapping("/in-progress")
    public List<TicketGetAllWhereStatusInProgressDTO> getAllInProgressTickets() {
        return ticketService.getAllInProgressTickets();
    }



    // in TicketController.java
    @GetMapping("/in-progress/{employeeId}")
    public TicketAssigmentDTO getInProgressTicketByEmployeeId(@PathVariable String employeeId) {
        return ticketService.getInProgressTicketByEmployeeId(employeeId);
    }

    // UPDATE for assigment
    @PutMapping("/next/{employeeId}")
    public TicketAssigmentDTO assignNextTicketToEmployee(@PathVariable String employeeId) {
        return ticketService.assignNextTicket(employeeId);
    }


    @PutMapping("/{ticketId}/status/{employeeId}")
    public TicketUpdateDTO updateTicketStatus(
            @PathVariable String ticketId,
            @PathVariable String employeeId,
            @RequestBody TicketUpdateDTO updateDTO) {

        return ticketService.updateTicketStatus(ticketId, employeeId, updateDTO);
    }

/*
    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable String id) {
        Optional<TicketGetByIdDTO> ticketOptional = ticketService.getTicketById(id);
        if (ticketOptional.isPresent()) {
            ticketService.deleteTicket(id);
        } else {
            throw new IllegalArgumentException("Ticket mit der id: " + id + " nicht gefunden");
        }
    }*/

    @DeleteMapping("/deleteAll")
    public void deleteAllTickets(){
        ticketService.deleteAllTicketsAndResetDepartmentNumbers();
    }


}
