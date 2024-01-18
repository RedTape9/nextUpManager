package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;

import java.util.List;

@Data
public class TicketAssigmentDTO {
    private String id;
    private String employeeId;
    private String room;
    private String currentStatus;
    private String timestamp;
    private List<StatusChangeDTO> statusHistory;
}

