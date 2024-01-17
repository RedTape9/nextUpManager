package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class TicketUpdateDTO {
    private String departmentId;  // optional, falls sie geändert werden soll
    private List<Ticket.StatusChange> statusHistory;
    private TicketStatus currentStatus;
    private String employeeId;
    private String commentByEmployee;
    private String room;
    private String customerNr;

    @Data
    public static class StatusChangeDTO {
        private TicketStatus status;
        private Date timestamp;
    }
}
