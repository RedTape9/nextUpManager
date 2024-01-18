package io.github.redtape9.nextupmanager.backend.service;


import io.github.redtape9.nextupmanager.backend.dto.TicketUpdateDTO;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.dto.TicketAssigmentDTO;
import io.github.redtape9.nextupmanager.backend.dto.StatusChangeDTO;
import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import io.github.redtape9.nextupmanager.backend.entity.Department;
import io.github.redtape9.nextupmanager.backend.entity.Employee;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.TicketRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;


import io.github.redtape9.nextupmanager.backend.utils.LocalDateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

import static io.github.redtape9.nextupmanager.backend.utils.LocalDateTimeFormatter.getFormattedDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    private final DepartmentService departmentService;



    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(String id) {
        return ticketRepository.findById(id);
    }

    //CREATE

    public Ticket createTicketWithDepartment(Ticket ticket, String departmentName) {
        // weitere DTO hier?
        Department department = departmentRepository.findByName(departmentName);
        if (department == null) {
            throw new IllegalArgumentException("Department mit Namen: " + departmentName + " existiert nicht");
        }

        // Logik zur Erstellung eines Tickets
        ticket.setDepartmentId(department.getId());
        ticket.setCreatedAt(getFormattedDateTime());
        ticket.setCurrentStatus(TicketStatus.WAITING);
        ticket.setTicketNr(department.getPrefix() + (department.getCurrentNumber() + 1));
        Ticket.StatusChange statusChange = new Ticket.StatusChange();
        statusChange.setStatus(TicketStatus.WAITING);
        statusChange.setTimestamp(ticket.getCreatedAt());
        ticket.getStatusHistory().add(statusChange);

        Ticket createdTicket = ticketRepository.save(ticket);


        // Aktualisieren der currentNumber in Department
        department.setCurrentNumber(department.getCurrentNumber() + 1);
        departmentService.updateDepartment(department);

        return createdTicket;
    }


    //UPDATE for simle version
    /*public Ticket updateTicket(String id, TicketCreateDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateTicketWithDTO(ticket, updateDTO);
        return ticketRepository.save(ticket);
    }


    private void updateTicketWithDTO(Ticket ticket, TicketCreateDTO updateDTO) {
        ticket.setDepartmentId(updateDTO.getDepartmentId());
        ticket.setStatusHistory(updateDTO.getStatusHistory());
        ticket.setCurrentStatus(updateDTO.getCurrentStatus());
        ticket.setEmployeeId(updateDTO.getEmployeeId());
        ticket.setCommentByEmployee(updateDTO.getCommentByEmployee());
    }
*/
    public void deleteTicket(String id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> getAllTicketsByDepartmentId(String departmentId) {
        return ticketRepository.findByDepartmentId(departmentId);
    }


    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    //UPDATE for assignment
    public TicketAssigmentDTO assignNextTicket(String employeeId) {
        /*Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket mit der ID: " + employeeId + " nicht gefunden"));

        Optional<Ticket> oldestTicketOptional = ticketRepository.findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(employee.getDepartmentId());

        if (oldestTicketOptional.isEmpty()) {
            throw new IllegalArgumentException("Keine Tickets im Wartestatus gefunden");
        }

        Ticket oldestTicket = oldestTicketOptional.get();*/

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket mit der ID: " + employeeId + " nicht gefunden"));

        Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt"));
        List<Ticket> tickets = ticketRepository.findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(employee.getDepartmentId(), pageable);

        if (tickets.isEmpty()) {
            throw new IllegalArgumentException("Keine Tickets im Wartestatus gefunden");
        }

        Ticket oldestTicket = tickets.getFirst();

        oldestTicket.setCurrentStatus(TicketStatus.IN_PROGRESS);
        Ticket.StatusChange newStatusChange = new Ticket.StatusChange();
        newStatusChange.setStatus(TicketStatus.IN_PROGRESS);
        newStatusChange.setTimestamp(getFormattedDateTime());
        oldestTicket.getStatusHistory().add(newStatusChange);

        oldestTicket.setRoom(employee.getRoom());
        oldestTicket.setEmployeeId(employeeId);

        Ticket updatedTicket = ticketRepository.save(oldestTicket);

        List<StatusChangeDTO> statusChangeDTOs = updatedTicket.getStatusHistory().stream()
                .map(sc -> {
                    StatusChangeDTO dto = new StatusChangeDTO();
                    dto.setStatus(sc.getStatus().toString());
                    dto.setTimestamp(sc.getTimestamp());
                    return dto;
                })
                .collect(Collectors.toList());

        TicketAssigmentDTO dto = new TicketAssigmentDTO();

        dto.setEmployeeId(updatedTicket.getEmployeeId());
        dto.setRoom(updatedTicket.getRoom());
        dto.setCurrentStatus(updatedTicket.getCurrentStatus().toString());
        dto.setTimestamp(newStatusChange.getTimestamp());
        dto.setStatusHistory(statusChangeDTOs);

        return dto;
    }

    //UPDATE for status change on FINISHED or CANCELED

    public Ticket updateTicketStatus(String ticketId, String employeeId, TicketUpdateDTO updateDTO) {


        TicketStatus newStatus;
        try {
            newStatus = TicketStatus.valueOf(updateDTO.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ungültiger Status: " + updateDTO.getStatus());
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket mit der ID: " + ticketId + " nicht gefunden"));

        if (!ticket.getEmployeeId().equals(employeeId) || ticket.getCurrentStatus() != TicketStatus.IN_PROGRESS) {
            throw new IllegalStateException("Ticket kann nicht aktualisiert werden, da der Mitarbeiter oder der Status nicht valide ist");
        }

        ticket.setCurrentStatus(newStatus);

        if (updateDTO.getCommentByEmployee() != null && !updateDTO.getCommentByEmployee().trim().isEmpty()) {
            String sanitizedComment = sanitizeInput(updateDTO.getCommentByEmployee());
            ticket.setCommentByEmployee(sanitizedComment);
        }

        // Hier setzen Sie den neuen Status und fügen ihn der History hinzu

        Ticket.StatusChange newStatusChange = new Ticket.StatusChange();
        newStatusChange.setStatus(newStatus);
        newStatusChange.setTimestamp(LocalDateTimeFormatter.getFormattedDateTime());

        ticket.getStatusHistory().add(newStatusChange);

        return ticketRepository.save(ticket);
    }

    private String sanitizeInput(String input) {
        // Bereinigen und Validieren der Eingabe
        // ...
        return input;
    }


}