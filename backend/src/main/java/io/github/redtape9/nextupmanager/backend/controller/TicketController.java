package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketUpdateDTO;
import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final DepartmentService departmentService;



    @PostMapping("/department/{name}")
    public Ticket createTicket(@RequestBody Ticket ticket, @PathVariable String name) {
        return ticketService.createTicketWithDepartment(ticket, name);
    }

    @GetMapping
    public List<Ticket> getAllCustomers() {
        return ticketService.getAllTickets();
    }

    @GetMapping("department/{name}")
    public List<Ticket> getCustomersByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }
        return ticketService.getTicketsByDepartment(department.getId());
    }

    @GetMapping("/department/{departmentId}")
    public List<Ticket> getCustomersByDepartment(@PathVariable String departmentId) {
        return ticketService.getTicketsByDepartment(departmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateCustomer(@PathVariable String id, @RequestBody TicketUpdateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            Ticket updatedTicket = ticketService.updateTicket(id, updateDTO);
            return ResponseEntity.ok(updatedTicket);
        } else {
            throw new IllegalArgumentException("Kundeneingaben sind nicht valide");
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
