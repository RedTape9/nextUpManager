package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.*;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    // CREATE
    @PostMapping("/department")
    public Ticket createTicket(@RequestBody TicketCreateDTO ticketCreateDTO) {
        return ticketService.createTicketWithDepartment(ticketCreateDTO);
    }

    @GetMapping("/waiting")
    public List<TicketGetAllDTO> getAllWaitingTickets() {
        return ticketService.getAllWaitingTickets();
    }

    @GetMapping("/in-progress")
    public List<TicketGetAllWhereStatusInProgressDTO> getAllInProgressTickets() {
        return ticketService.getAllInProgressTickets();
    }


    @GetMapping("/in-progress/{employeeId}")
    public TicketAssigmentDTO getInProgressTicketByEmployeeId(@PathVariable String employeeId) {
        return ticketService.getInProgressTicketByEmployeeId(employeeId);
    }

    // UPDATE for assigment
    @PutMapping("/next/{employeeId}")
    public void assignNextTicketToEmployee(@PathVariable String employeeId) {
        ticketService.assignNextTicket(employeeId);
    }


    @PutMapping("/{ticketId}")
    public void updateTicketStatus(
            @PathVariable String ticketId,
            @RequestBody TicketUpdateDTO updateDTO) {

       ticketService.updateTicket(ticketId, updateDTO);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllTickets(){
        ticketService.deleteAllTicketsAndResetDepartmentNumbers();
    }


}
