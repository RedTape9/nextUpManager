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
        department.setId("659e901e44c1ebb1ea8755e9");

        ticket = new Ticket();
        ticket.setId("1");

    }

    @Test
    public void getAllWaitingTickets_returnsAllWaitingTickets() {
        List<Ticket> tickets = new ArrayList<>();
        when(ticketRepository.findAllByCurrentStatus(TicketStatus.WAITING)).thenReturn(tickets);

        List<TicketGetAllDTO> result = ticketService.getAllWaitingTickets();

        assertEquals(tickets.size(), result.size());
        verify(ticketRepository, times(1)).findAllByCurrentStatus(TicketStatus.WAITING);
    }

    @Test
    public void getInProgressTicketByEmployeeId_returnsTicket_whenTicketExists() {
        String employeeId = "1";
        Ticket ticket = new Ticket();
        when(ticketRepository.findByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(Optional.of(ticket));

        TicketAssigmentDTO result = ticketService.getInProgressTicketByEmployeeId(employeeId);

        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
    }

    @Test
    public void getInProgressTicketByEmployeeId_throwsException_whenTicketDoesNotExist() {
        String employeeId = "1";
        when(ticketRepository.findByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ticketService.getInProgressTicketByEmployeeId(employeeId));
    }

    /*@Test
    public void testCreateTicketWithDepartment_Successful() {
        // Arrange
        when(departmentRepository.findById(anyString())).thenReturn(Optional.of(department));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        // Act
        Ticket createdTicket = ticketService.createTicketWithDepartment(ticketCreateDTO);

        // Assert
        assertNotNull(createdTicket);
        assertEquals("659e901e44c1ebb1ea8755e9", createdTicket.getDepartmentId());
        verify(departmentRepository, times(1)).findById("659e901e44c1ebb1ea8755e9");
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(messagingTemplate).convertAndSend("/topic/updates", "Neues Ticket erstellt: " + createdTicket.getTicketNr());
    }*/

    @Test
    void testCreateTicketWithDepartment_Successful() {
        // Setup
        TicketCreateDTO ticketCreateDTO = new TicketCreateDTO();
        ticketCreateDTO.setDepartmentId("659e901e44c1ebb1ea8755e8");

        Department mockDepartment = new Department();
        mockDepartment.setId("659e901e44c1ebb1ea8755e8");
        mockDepartment.setName("Einwohnermeldeamt");
        mockDepartment.setPrefix("E-");
        mockDepartment.setCurrentNumber(111);

        when(departmentRepository.findById("659e901e44c1ebb1ea8755e8")).thenReturn(Optional.of(mockDepartment));
        when(departmentRepository.save(any(Department.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArguments()[0]);

        // Erwartete Zeit etwas vor der aktuellen Zeit, um sicherzustellen, dass sie vor dem Erstellen des Tickets liegt
        LocalDateTime expectedTimeBeforeNow = LocalDateTime.now().minusSeconds(1);

        // Ausführen
        Ticket result = ticketService.createTicketWithDepartment(ticketCreateDTO);

        // Verifizieren
        assertNotNull(result);
        assertEquals("65ba6ca23a03d73d9d27c996", result.getId());
        assertEquals("659e901e44c1ebb1ea8755e8", result.getDepartmentId());
        assertEquals("E-112", result.getTicketNr());
        assertEquals(TicketStatus.WAITING, result.getCurrentStatus());

        assertNotNull(result.getStatusHistory());
        assertFalse(result.getStatusHistory().isEmpty());
        StatusChange statusChange = result.getStatusHistory().get(0);
        assertTrue(LocalDateTime.now().isAfter(expectedTimeBeforeNow));
        assertTrue(expectedTimeBeforeNow.isBefore(LocalDateTime.now()));

        assertNull(result.getEmployeeId());
        assertNull(result.getRoom());
        assertNull(result.getCommentByEmployee());

        // Aufräumen und überprüfen der Interaktionen
        verify(departmentRepository).findById("659e901e44c1ebb1ea8755e8");
        verify(ticketRepository).save(any(Ticket.class));
        // ... und weitere Verifications nach Bedarf
    }





    @Test
    public void testCreateTicketWithDepartment_DepartmentNotFound() {
        when(departmentRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.createTicketWithDepartment(ticketCreateDTO);
        });
    }

    @Test
    public void assignNextTicket_assignsTicket() {
        String employeeId = "1";
        Employee employee = new Employee();
        Ticket ticket = new Ticket();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(ticketRepository.findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(employee.getDepartmentId(), TicketStatus.WAITING)).thenReturn(Optional.of(ticket));

        assertDoesNotThrow(() -> ticketService.assignNextTicket(employeeId));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    public void assignNextTicket_throwsException_whenEmployeeHasActiveTicket() {
        String employeeId = "1";
        when(ticketRepository.existsByEmployeeIdAndCurrentStatus(employeeId, TicketStatus.IN_PROGRESS)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> ticketService.assignNextTicket(employeeId));
    }

    @Test
    public void updateTicket_updatesTicket() {
        String ticketId = "1";
        TicketUpdateDTO updateDTO = new TicketUpdateDTO();
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        assertDoesNotThrow(() -> ticketService.updateTicket(ticketId, updateDTO));
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    public void updateTicket_throwsException_whenTicketDoesNotExist() {
        String ticketId = "1";
        TicketUpdateDTO updateDTO = new TicketUpdateDTO();
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ticketService.updateTicket(ticketId, updateDTO));
    }

    @Test
    public void deleteAllTicketsAndResetDepartmentNumbers_deletesAllTicketsAndResetsDepartmentNumbers() {
        List<Department> departments = new ArrayList<>();
        when(departmentRepository.findAll()).thenReturn(departments);

        assertDoesNotThrow(() -> ticketService.deleteAllTicketsAndResetDepartmentNumbers());
        verify(ticketRepository, times(1)).deleteAll();
    }
}