package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.entity.Department;
import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final TicketService ticketService;

    @GetMapping("/{name}")
    public List<Ticket> getTicketsByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department mit namen: " + name + " existiert nicht");
        }
        return ticketService.getAllTicketsByDepartmentId(department.getId());
    }

    @GetMapping("/id/{departmentId}")
    public List<Ticket> getTicketsByDepartment(@PathVariable String departmentId) {
        return ticketService.getAllTicketsByDepartmentId(departmentId);
    }

    // GET
    @GetMapping
    public List<DepartmentGetForOptionDTO> getDepartments() {
        return departmentService.getAllDepartments();
    }


}