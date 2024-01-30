package io.github.redtape9.nextupmanager.backend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Document(collection = "tickets")
public class Ticket {
    @Id
    private String id;
    private String departmentId;
    private String ticketNr;
    private List<StatusChange> statusHistory = new ArrayList<>();
    private TicketStatus currentStatus;
    private LocalDateTime createdAt;
    private String employeeId;
    private String room;
    private String commentByEmployee;
    @Data
    public static class StatusChange {
        private TicketStatus status;
        private LocalDateTime timestamp;
    }

}
