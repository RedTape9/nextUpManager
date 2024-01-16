package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;


@Data
public class TicketUpdateDTO {
    private TicketStatus status;
    private String employeeId;
}

