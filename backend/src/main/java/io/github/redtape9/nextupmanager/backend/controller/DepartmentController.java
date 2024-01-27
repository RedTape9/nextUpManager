package io.github.redtape9.nextupmanager.backend.controller;

import io.github.redtape9.nextupmanager.backend.entity.Department;
import io.github.redtape9.nextupmanager.backend.dto.DepartmentGetForOptionDTO;
import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;
    private final TicketService ticketService;

    @GetMapping("/name/{name}")
    public List<Ticket> getTicketsByDepartmentName(@PathVariable String name) {
        Department department = departmentService.getDepartmentByName(name);
        if (department == null) {
            throw new IllegalArgumentException("Department mit namen: " + name + " existiert nicht");
        }
        return ticketService.getAllTicketsByDepartmentId(department.getId());
    }
    //TODO: umschreiben nach TicketController
    @GetMapping("/id/{departmentId}")
    public List<Ticket> getTicketsByDepartment(@PathVariable String departmentId) {
        return ticketService.getAllTicketsByDepartmentId(departmentId);
    }

    // GET
    @GetMapping
    public List<DepartmentGetForOptionDTO> getDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentGetForOptionDTO getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id);
    }


}
