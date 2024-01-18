package io.github.redtape9.nextupmanager.backend.dto;

import io.github.redtape9.nextupmanager.backend.model.Ticket;
import io.github.redtape9.nextupmanager.backend.model.TicketStatus;
import lombok.Data;
import java.util.List;

@Data
public class TicketCreateDTO {
    private String departmentId;  // optional, falls sie geändert werden soll
    private TicketStatus currentStatus;
    private String employeeId;
    private String commentByEmployee;
    private String room;
    private String customerNr;
    private List<Ticket.StatusChange> statusHistory;

}

// dont forget to remove unnecessary fields