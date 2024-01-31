package io.github.redtape9.nextupmanager.backend.dto;

import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import lombok.Data;
import java.util.List;

@Data
public class TicketCreateDTO {

    private String departmentId;
    private String createdAt;
    private TicketStatus currentStatus;
    private String ticketNr;
    private List<Ticket.StatusChange> statusHistory;

}
