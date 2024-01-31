package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.dto.*;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;


    // CREATE
    @PostMapping("/department/{name}")
    public Ticket createTicket(@RequestBody TicketCreateDTO ticketCreateDTO, @PathVariable String name) {
        return ticketService.createTicketWithDepartment(ticketCreateDTO, name);
    }

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

    @DeleteMapping("/deleteAll")
    public void deleteAllTickets(){
        ticketService.deleteAllTicketsAndResetDepartmentNumbers();
    }


}
