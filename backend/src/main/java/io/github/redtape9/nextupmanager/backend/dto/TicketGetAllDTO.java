package io.github.redtape9.nextupmanager.backend.dto;
import lombok.Data;

@Data
public class TicketGetAllDTO {
    private String id;
    private String departmentId;
    private String ticketNr;
    private String currentStatus;

}
