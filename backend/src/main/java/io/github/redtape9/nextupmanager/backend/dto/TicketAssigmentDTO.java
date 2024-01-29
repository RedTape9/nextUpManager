package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketAssigmentDTO {
    private String id;
    private String employeeId;
    private String room;
    private String currentStatus;
    private LocalDateTime timestamp;
    private String ticketNr;
    private List<StatusChangeDTO> statusHistory;
}

