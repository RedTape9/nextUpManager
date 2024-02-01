package io.github.redtape9.nextupmanager.backend;

import io.github.redtape9.nextupmanager.backend.dto.*;
import io.github.redtape9.nextupmanager.backend.entity.*;
import io.github.redtape9.nextupmanager.backend.repo.DepartmentRepository;
import io.github.redtape9.nextupmanager.backend.repo.TicketRepository;
import io.github.redtape9.nextupmanager.backend.repo.EmployeeRepository;
import io.github.redtape9.nextupmanager.backend.service.DepartmentService;
import io.github.redtape9.nextupmanager.backend.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TicketService ticketService;


    TicketCreateDTO ticketCreateDTO;
    Department department;
    Ticket ticket;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setDepartmentId("659e901e44c1ebb1ea8755e9");

        department = new Department();
        department.setId("659e901e44c1ebb1ea8755e8");

        ticket = new Ticket();
        ticket.setId("1");

    }

    @Test
    void getAllWaitingTickets_ShouldReturnAllWaitingTickets() {
        // Given
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = new Ticket();
        ticket1.setId("65ba76dd4ba9774dec11ddbd");
        ticket1.setDepartmentId("659e901e44c1ebb1ea8755e8");
        ticket1.setTicketNr("E-101");
        ticket1.setCurrentStatus(TicketStatus.WAITING);
        tickets.add(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setId("65ba76fb4ba9774dec11ddbe");
        ticket2.setDepartmentId("659e901e44c1ebb1ea8755e8");
        ticket2.setTicketNr("E-102");
        ticket2.setCurrentStatus(TicketStatus.WAITING);
        tickets.add(ticket2);

        when(ticketRepository.findAllByCurrentStatus(TicketStatus.WAITING)).thenReturn(tickets);

        // When
        List<TicketGetAllDTO> result = ticketService.getAllWaitingTickets();

        // Then
        assertEquals(2, result.size());
        assertEquals("E-101", result.get(0).getTicketNr());
        assertEquals("E-102", result.get(1).getTicketNr());
        verify(ticketRepository).findAllByCurrentStatus(TicketStatus.WAITING);
    }

    @Test
    void getEmployeeById_ShouldReturnEmployee_WhenEmployeeExists() {
        // Given
        String employeeId = "65a65442a26af860b1c0681a";
        Employee expectedEmployee = new Employee();
        expectedEmployee.setId(employeeId);
        expectedEmployee.setName("Dieter");
        expectedEmployee.setSurname("Bohlen");
        expectedEmployee.setDepartmentId("659e901e44c1ebb1ea8755e8");
        expectedEmployee.setRoom("E21");
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(expectedEmployee));

        // When
        Employee actualEmployee = employeeRepository.findById(employeeId).orElse(null);

        // Then
        assertEquals(expectedEmployee, actualEmployee);
    }


    @Test
    void getOldestTicket_ShouldReturnOldestTicket_WhenTicketExists() {
        // Given
        String departmentId = "659e901e44c1ebb1ea8755e8";
        Employee employee = new Employee();
        employee.setId("employeeId");
        employee.setName("Max");
        employee.setSurname("Mustermann");
        employee.setDepartmentId(departmentId);
        employee.setRoom("Room1");

        Ticket oldestTicket = new Ticket();
        oldestTicket.setId("65ba76dd4ba9774dec11ddbd");
        oldestTicket.setDepartmentId(departmentId);
        oldestTicket.setTicketNr("E-101");
        oldestTicket.setCurrentStatus(TicketStatus.WAITING);
        when(ticketRepository.findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(departmentId, TicketStatus.WAITING)).thenReturn(Optional.of(oldestTicket));

        // When
        Ticket result = ticketService.getOldestTicket(employee);

        // Then
        assertEquals(oldestTicket, result);
    }

    @Test
    void getOldestTicket_ShouldThrowIllegalArgumentException_WhenNoTicketFound() {
        // Given
        String departmentId = "659e901e44c1ebb1ea8755e8";
        Employee employee = new Employee();
        employee.setId("employeeId");
        employee.setName("Max");
        employee.setSurname("Mustermann");
        employee.setDepartmentId(departmentId);
        employee.setRoom("Room1");
        when(ticketRepository.findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(departmentId, TicketStatus.WAITING)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> ticketService.getOldestTicket(employee));
    }

    @Test
    void getAllInProgressTickets_ShouldReturnAllInProgressTickets() {
        // Given
        List<Ticket> tickets = new ArrayList<>();
        Ticket ticket1 = new Ticket();
        ticket1.setId("65ba76dd4ba9774dec11ddbd");
        ticket1.setDepartmentId("659e901e44c1ebb1ea8755e8");
        ticket1.setTicketNr("E-101");
        ticket1.setCurrentStatus(TicketStatus.IN_PROGRESS);
        tickets.add(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setId("65ba76fb4ba9774dec11ddbe");
        ticket2.setDepartmentId("659e901e44c1ebb1ea8755e8");
        ticket2.setTicketNr("E-102");
        ticket2.setCurrentStatus(TicketStatus.IN_PROGRESS);
        tickets.add(ticket2);

        when(ticketRepository.findAllByCurrentStatus(TicketStatus.IN_PROGRESS)).thenReturn(tickets);

        // When
        List<TicketGetAllWhereStatusInProgressDTO> result = ticketService.getAllInProgressTickets();

        // Then
        assertEquals(2, result.size());
        assertEquals("E-101", result.get(0).getTicketNr());
        assertEquals("E-102", result.get(1).getTicketNr());
        verify(ticketRepository).findAllByCurrentStatus(TicketStatus.IN_PROGRESS);
    }

    @Test
    void getInProgressTicketByEmployeeId_ShouldReturnTicket_WhenTicketExists() {
        // Given
        String employeeId = "65a65442a26af860b1c0681a";
        Ticket ticket = new Ticket();
        ticket.setId("65ba76dd4ba9774dec11ddbd");

        ticket.setRoom("E21");
        ticket.setEmployeeId(employeeId);
        when(ticketRepository.findByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(Optional.of(ticket));

        // When
        TicketAssigmentDTO result = ticketService.getInProgressTicketByEmployeeId(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
        assertEquals(ticket.getRoom(), result.getRoom());
        assertEquals(ticket.getTicketNr(), result.getTicketNr());
        assertEquals(ticket.getCurrentStatus(), result.getCurrentStatus());
        assertEquals(ticket.getEmployeeId(), result.getEmployeeId());
    }

    @Test
    void getInProgressTicketByEmployeeId_ShouldReturnNull_WhenTicketNotFound() {
        // Given
        String employeeId = "non-existing-employee-id";
        when(ticketRepository.findByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(Optional.empty());

        // When & Then
        assertNull(ticketService.getInProgressTicketByEmployeeId(employeeId));
    }

    @Test
    void createTicketWithDepartment_ShouldCreateTicket() {
        // Given
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setDepartmentId("659e901e44c1ebb1ea8755e8");
        Department department = new Department();
        department.setId("659e901e44c1ebb1ea8755e8");
        department.setName("IT");
        department.setPrefix("IT-");
        department.setCurrentNumber(111);

        LocalDateTime now = LocalDateTime.now();
        Ticket expectedTicket = new Ticket();
        expectedTicket.setDepartmentId("659e901e44c1ebb1ea8755e8");
        expectedTicket.setTicketNr("IT-112");
        expectedTicket.setCurrentStatus(TicketStatus.WAITING);
        expectedTicket.setCreatedAt(now);
        expectedTicket.setStatusHistory(new ArrayList<>(List.of(new StatusChange(TicketStatus.WAITING, now))));

        when(departmentRepository.findById("659e901e44c1ebb1ea8755e8")).thenReturn(Optional.of(department));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> {
            Ticket ticket = i.getArgument(0, Ticket.class);
            ticket.setId("65ba6ca23a03d73d9d27c996");
            return ticket;
        });

        // When
        Ticket result = ticketService.createTicketWithDepartment(ticketCreateDTO);

        // Then
        assertNotNull(result);
        assertEquals("65ba6ca23a03d73d9d27c996", result.getId());
        assertEquals(expectedTicket.getDepartmentId(), result.getDepartmentId());
        assertEquals(expectedTicket.getTicketNr(), result.getTicketNr());
        assertEquals(expectedTicket.getCurrentStatus(), result.getCurrentStatus());
        assertEquals(expectedTicket.getStatusHistory().size(), result.getStatusHistory().size());
    }


    @Test
    void createTicketWithDepartment_ShouldThrowIllegalArgumentException_WhenDepartmentNotFound() {
        // Given
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setDepartmentId("non-existing-department-id");
        when(departmentRepository.findById("non-existing-department-id")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> ticketService.createTicketWithDepartment(ticketCreateDTO));
    }
    @Test
    void assignNextTicket_ShouldAssignEmployeeToTicket() {
        // Given
        String employeeId = "65a65442a26af860b1c0681a";
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setName("Max");
        employee.setSurname("Mustermann");
        employee.setRoom("X21");
        employee.setDepartmentId("659e901e44c1ebb1ea8755b1");

        Ticket oldestTicket = new Ticket();
        oldestTicket.setId("65ba76dd4ba9774dec11dd33");
        oldestTicket.setDepartmentId("659e901e44c1ebb1ea8755b1");
        oldestTicket.setTicketNr("X-101");
        oldestTicket.setCurrentStatus(TicketStatus.WAITING);
        oldestTicket.setCreatedAt(LocalDateTime.now().minusDays(5));

        ticketService.updateTicketStatus(oldestTicket, employee, TicketStatus.IN_PROGRESS);
        ticketRepository.save(oldestTicket);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(employee.getDepartmentId(), TicketStatus.WAITING)).thenReturn(Optional.of(oldestTicket));
        when(ticketRepository.existsByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(false);

        // When
        ticketService.assignNextTicket(employeeId);

        // Then
        assertEquals(TicketStatus.IN_PROGRESS, oldestTicket.getCurrentStatus());
        assertEquals(employeeId, oldestTicket.getEmployeeId());
        assertEquals(employee.getRoom(), oldestTicket.getRoom());
    }


    @Test
    void assignNextTicket_ShouldThrowIllegalStateException_WhenEmployeeAlreadyHasActiveTicket() {
        // Given
        String employeeId = "employeeId";
        when(ticketRepository.existsByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(true);

        // When & Then
        assertThrows(IllegalStateException.class, () -> ticketService.assignNextTicket(employeeId));
    }

    @Test
    void updateTicket_ShouldUpdateTicketStatus() {
        // Given
        String ticketId = "ticketId";
        TicketUpdateDTO updateDTO = new TicketUpdateDTO();
        updateDTO.setCommentByEmployee("New Comment");
        updateDTO.setCurrentStatus(TicketStatus.FINISHED);

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setCurrentStatus(TicketStatus.IN_PROGRESS);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // When
        ticketService.updateTicket(ticketId, updateDTO);

        // Then
        verify(ticketRepository).save(ticket);
        assertEquals(TicketStatus.FINISHED, ticket.getCurrentStatus());
        assertEquals("New Comment", ticket.getCommentByEmployee());
    }

    @Test
    void updateTicket_ShouldThrowIllegalArgumentException_WhenTicketNotFound() {
        // Given
        String ticketId = "non-existing-ticket-id";
        TicketUpdateDTO updateDTO = new TicketUpdateDTO();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> ticketService.updateTicket(ticketId, updateDTO));
    }

    @Test
    void deleteAllTicketsAndResetDepartmentNumbers_ShouldDeleteAllTicketsAndResetNumbers() {
        // Given
        List<Department> departments = new ArrayList<>();
        when(departmentRepository.findAll()).thenReturn(departments);

        // When
        ticketService.deleteAllTicketsAndResetDepartmentNumbers();

        // Then
        verify(ticketRepository).deleteAll();
        verify(departmentRepository).findAll();
        for (Department department : departments) {
            assertEquals(100, department.getCurrentNumber());
        }
    }

    @Test
    void updateTicket_ShouldThrowIllegalArgumentException_WhenTicketStatusNotValid() {
        // Given
        String ticketId = "65ba76dd4ba9774dec11ddbd";
        TicketUpdateDTO updateDTO = new TicketUpdateDTO();
        updateDTO.setCurrentStatus(TicketStatus.FINISHED); // Ein Status, der nicht erlaubt ist

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setCurrentStatus(TicketStatus.WAITING); // Ein anderer Status als im updateDTO

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> ticketService.updateTicket(ticketId, updateDTO));
        assertEquals("Ticket kann nicht aktualisiert werden, da der Status nicht valide ist", thrown.getMessage());
    }
}