package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketGetByIdDTO {
    private String id;
    private String departmentId;
    private String ticketNr;
    private String room;
    private String employeeId;
    private String commentByEmployee;
    private String currentStatus;
    private List<StatusChangeDTO> statusHistory;
}
