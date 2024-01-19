package io.github.redtape9.nextupmanager.backend.dto;
import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import lombok.Data;

@Data
public class TicketGetAllDTO {
    private String id;
    private String departmentId;
    private String ticketNr;
    private String currentStatus;

}
