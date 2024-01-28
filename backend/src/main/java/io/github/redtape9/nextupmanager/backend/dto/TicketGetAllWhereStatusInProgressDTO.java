package io.github.redtape9.nextupmanager.backend.dto;
import lombok.Data;

@Data
public class TicketGetAllWhereStatusInProgressDTO {
    private String ticketNr;
    private String room;

}