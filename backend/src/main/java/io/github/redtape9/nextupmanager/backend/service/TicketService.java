package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.model.TicketStatus;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.repo.TicketRepository;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static io.github.redtape9.nextupmanager.backend.utils.LocalDateTimeFormatter.getFormattedDateTime;


@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository nextUpTicketRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    public List<Ticket> getAllTickets() {
        return nextUpTicketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(String id) {
        return nextUpTicketRepository.findById(id);
    }

    public Ticket createTicketWithDepartment(Ticket ticket, String departmentName) {
        Department department = departmentRepository.findByName(departmentName);
        if (department == null) {
            throw new IllegalArgumentException("Department with name: " + departmentName + " does not exist");
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

        Ticket createdTicket = nextUpTicketRepository.save(ticket);

        // Aktualisieren der currentNumber in Department
        department.setCurrentNumber(department.getCurrentNumber() + 1);
        departmentService.updateDepartment(department);

        return createdTicket;
    }


    public Ticket updateTicket(String id, TicketUpdateDTO updateDTO) {
        Ticket ticket = nextUpTicketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateTicketWithDTO(ticket, updateDTO);
        return nextUpTicketRepository.save(ticket);
    }

    private void updateTicketWithDTO(Ticket ticket, TicketUpdateDTO updateDTO) {
        ticket.setDepartmentId(updateDTO.getDepartmentId());
        ticket.setStatusHistory(updateDTO.getStatusHistory());
        ticket.setCurrentStatus(updateDTO.getCurrentStatus());
        ticket.setEmployeeId(updateDTO.getEmployeeId());
        ticket.setCommentByEmployee(updateDTO.getCommentByEmployee());
    }

    public void deleteCustomer(String id) {
        nextUpTicketRepository.deleteById(id);
    }

    public List<Ticket> getTicketsByDepartment(String departmentId) {
        return nextUpTicketRepository.findByDepartmentId(departmentId);
    }


    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }


}
