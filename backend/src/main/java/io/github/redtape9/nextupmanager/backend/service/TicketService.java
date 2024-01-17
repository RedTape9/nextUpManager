package io.github.redtape9.nextupmanager.backend.service;

import io.github.redtape9.nextupmanager.backend.model.Department;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.repo.TicketRepository;
import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository nextUpTicketRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<Ticket> getAllCustomers() {
        return nextUpTicketRepository.findAll();
    }

    public Optional<Ticket> getCustomerById(String id) {
        return nextUpTicketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        return nextUpTicketRepository.save(ticket);
    }

    public Ticket updateCustomer(String id, TicketUpdateDTO updateDTO) {
        Ticket ticket = nextUpTicketRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kunde mit der id: " + id + " nicht gefunden"));
        updateCustomerWithDTO(ticket, updateDTO);
        return nextUpTicketRepository.save(ticket);
    }

    private void updateCustomerWithDTO(Ticket ticket, TicketUpdateDTO updateDTO) {
        ticket.setDepartmentId(updateDTO.getDepartmentId());
        ticket.setStatusHistory(updateDTO.getStatusHistory());
        ticket.setCurrentStatus(updateDTO.getCurrentStatus());
        ticket.setEmployeeId(updateDTO.getEmployeeId());
        ticket.setCommentByEmployee(updateDTO.getCommentByEmployee());
    }

    public void deleteCustomer(String id) {
        nextUpTicketRepository.deleteById(id);
    }

    public List<Ticket> getCustomersByDepartment(String departmentId) {
        return nextUpTicketRepository.findByDepartmentId(departmentId);
    }


    public Department getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }


}
