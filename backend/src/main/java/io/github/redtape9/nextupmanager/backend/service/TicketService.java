package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.dto.*;
import io.github.redtape9.nextupmanager.backend.entity.*;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.TicketRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;
    private final SimpMessagingTemplate messagingTemplate;

    public List<TicketGetAllDTO> getAllWaitingTickets() {
        return ticketRepository.findAllByCurrentStatus(TicketStatus.WAITING)
                .stream()
                .map(ticket -> {
                    TicketGetAllDTO dto = new TicketGetAllDTO();
                    dto.setId(ticket.getId());
                    dto.setDepartmentId(ticket.getDepartmentId());
                    dto.setTicketNr(ticket.getTicketNr());
                    dto.setCurrentStatus(ticket.getCurrentStatus());
                    System.out.println("GetAllWaitingTickets Ausgabe: " + dto);
                    return dto;
                })
                .toList();
    }

    public List<TicketGetAllWhereStatusInProgressDTO> getAllInProgressTickets() {
        return ticketRepository.findAllByCurrentStatus(TicketStatus.IN_PROGRESS)
                .stream()
                .map(ticket -> {
                    TicketGetAllWhereStatusInProgressDTO dto = new TicketGetAllWhereStatusInProgressDTO();
                    dto.setTicketNr(ticket.getTicketNr());
                    dto.setRoom(ticket.getRoom());
                    System.out.println("GetAllInProgressTickets Ausgabe: " + dto);
                    return dto;
                })
                .toList();
    }

    public TicketAssigmentDTO getInProgressTicketByEmployeeId(String employeeId) {
        Optional<Ticket> ticketOptional = ticketRepository.findByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS);
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            TicketAssigmentDTO dto = new TicketAssigmentDTO();
            dto.setId(ticket.getId());
            dto.setEmployeeId(ticket.getEmployeeId());
            dto.setRoom(ticket.getRoom());
            dto.setCurrentStatus(ticket.getCurrentStatus());
            dto.setTicketNr(ticket.getTicketNr());
            System.out.println("GetInProgressTicketByEmployeeId Ausgabe: " + dto);

            return dto;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket nicht gefunden");
        }
    }

    public Ticket createTicketWithDepartment(TicketCreateDTO ticketCreateDTO) {
        Ticket preparedTicket = prepareTicket(ticketCreateDTO);
        Ticket createdTicket = saveTicket(preparedTicket);
        sendNotification(createdTicket);
        System.out.println("createdTicket = " + createdTicket);
        return createdTicket;
    }

    private Department getDepartmentById(String departmentId) {
        System.out.println("departmentId = " + departmentRepository.findById(departmentId));
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department mit ID: " + departmentId + " existiert nicht"));
    }

    private Ticket prepareTicket(TicketCreateDTO ticketCreateDTO) {

        Department department = getDepartmentById(ticketCreateDTO.getDepartmentId());
        Ticket ticket = new Ticket();
        ticket.setDepartmentId(ticketCreateDTO.getDepartmentId());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setCurrentStatus(TicketStatus.WAITING);
        department.setCurrentNumber(department.getCurrentNumber() + 1);
        departmentService.updateDepartment(department);
        ticket.setTicketNr(department.getPrefix() + department.getCurrentNumber());
        ticket.setStatusHistory(new ArrayList<>());
        ticket.getStatusHistory().add(new StatusChange(TicketStatus.WAITING, ticket.getCreatedAt()));

        System.out.println("prepareTicket Ausgabe: " + ticket);

        return ticket;

    }


    private Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }


    private void sendNotification(Ticket ticket) {
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/updates", "Neues Ticket erstellt: " + ticket.getTicketNr());
        } else {
            throw new IllegalStateException("SimpMessagingTemplate ist null");
        }
    }

    public void assignNextTicket(String employeeId) {
        checkActiveTicket(employeeId);
        Employee employee = getEmployeeById(employeeId);
        Ticket oldestTicket = getOldestTicket(employee);
        updateTicketStatus(oldestTicket, employee, TicketStatus.IN_PROGRESS);
        Ticket updatedTicket = saveTicket(oldestTicket);
        sendNotification(updatedTicket);
    }

    private void checkActiveTicket(String employeeId) {
        boolean hasActiveTicket = ticketRepository.existsByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS);
        if (hasActiveTicket) {
            throw new IllegalStateException("Mitarbeiter hat bereits ein aktives Ticket in Bearbeitung");
        }
    }

    private Employee getEmployeeById(String employeeId) {
        System.out.println("getEmployeeById Ausgabe: " + employeeRepository.findById(employeeId));
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Mitarbeiter mit der ID: " + employeeId + " nicht gefunden"));
    }

    private Ticket getOldestTicket(Employee employee) {
        Optional<Ticket> oldestTicket = ticketRepository.findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(employee.getDepartmentId(), TicketStatus.WAITING);
        System.out.println("getOldestTicket Ausgabe: " + oldestTicket);
        return oldestTicket.orElseThrow(() -> new IllegalArgumentException("Kein Ticket im Wartestatus gefunden"));
    }

    private void updateTicketStatus(Ticket ticket, Employee employee, TicketStatus status) {
        ticket.setCurrentStatus(status);
        ticket.setRoom(employee.getRoom());
        ticket.setEmployeeId(employee.getId());
        ticket.getStatusHistory().add(new StatusChange(TicketStatus.IN_PROGRESS, LocalDateTime.now()));
    }


    public void updateTicket(String ticketId, TicketUpdateDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket mit der ID: " + ticketId + " nicht gefunden"));
        if(ticket.getCurrentStatus() == TicketStatus.IN_PROGRESS)
        {
            ticket.setCommentByEmployee(updateDTO.getCommentByEmployee());
            ticket.setCurrentStatus(updateDTO.getCurrentStatus());
            ticket.getStatusHistory().add(new StatusChange(updateDTO.getCurrentStatus(), LocalDateTime.now()));
            System.out.println("updateTicket Ausgabe: " + ticket);
            ticketRepository.save(ticket);
            return;
        }
        throw new IllegalArgumentException("Ticket kann nicht aktualisiert werden, da der Status nicht valide ist");
    }

    public void deleteAllTicketsAndResetDepartmentNumbers() {
        ticketRepository.deleteAll();
        List<Department> departments = departmentRepository.findAll();
        for (Department department : departments) {
            department.setCurrentNumber(100);
            departmentService.updateDepartment(department);
        }
    }
}