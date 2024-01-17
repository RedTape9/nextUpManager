package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketUpdateDTO;
import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.model.TicketStatus;
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


    private static String getFormattedDateTime() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Datum und Uhrzeit im ISO-8601-Format formatieren und zurückgeben
        return now.format(formatter);
    }

    @PostMapping("/department/{name}")
    public Ticket createTicket(@RequestBody Ticket ticket, @PathVariable String name) {
        // Abteilung basierend auf dem Namen holen
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }

        // Kundeninformationen setzen
        ticket.setDepartmentId(department.getId());
        ticket.setCreatedAt(getFormattedDateTime());
        ticket.setCurrentStatus(TicketStatus.WAITING);
        ticket.setTicketNr(department.getPrefix() + (department.getCurrentNumber() + 1));

        // Eintrag in statusHistory hinzufügen
        Ticket.StatusChange statusChange = new Ticket.StatusChange();
        statusChange.setStatus(TicketStatus.WAITING);
        statusChange.setTimestamp(ticket.getCreatedAt());
        ticket.getStatusHistory().add(statusChange);

        // Neuen Kunden in der Datenbank speichern
        Ticket createdTicket = ticketService.createTicket(ticket);

        // currentNumber in der Abteilung aktualisieren
        department.setCurrentNumber(department.getCurrentNumber() + 1);
        departmentService.updateDepartmentByName(department.getName(), department);

        return createdTicket;
    }

    @GetMapping
    public List<Ticket> getAllCustomers() {
        return ticketService.getAllCustomers();
    }

    @GetMapping("department/{name}")
    public List<Ticket> getCustomersByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + name + " does not exist");
        }
        return ticketService.getCustomersByDepartment(department.getId());
    }

    @GetMapping("/department/{departmentId}")
    public List<Ticket> getCustomersByDepartment(@PathVariable String departmentId) {
        return ticketService.getCustomersByDepartment(departmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ticket> updateCustomer(@PathVariable String id, @RequestBody TicketUpdateDTO updateDTO) {
        if(isValidCustomerUpdateDTO(updateDTO)) {
            Ticket updatedTicket = ticketService.updateCustomer(id, updateDTO);
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
        Optional<Ticket> customerOptional = ticketService.getCustomerById(id);
        if (customerOptional.isPresent()) {
            ticketService.deleteCustomer(id);
        } else {
            throw new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden");
        }
    }


}
