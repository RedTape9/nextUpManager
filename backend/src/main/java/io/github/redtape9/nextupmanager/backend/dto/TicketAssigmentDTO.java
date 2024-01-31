package io.github.redtape9.nextupmanager.backend.dto;

import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import lombok.Data;

import java.util.List;

@Data
public class TicketAssigmentDTO {
    private String id;
    private String employeeId;
    private String room;
    private TicketStatus currentStatus;
    private String ticketNr;
    private List<StatusChangeDTO> statusHistory;
}

