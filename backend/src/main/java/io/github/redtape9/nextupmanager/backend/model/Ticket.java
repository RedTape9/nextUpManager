package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Document(collection = "tickets")
public record Ticket(
        @Id
        String id,
        String employeeId,
        String departmentId,
        String customerId,
        String currentNumber,
        LocalDateTime createdAt,
        TicketStatus status
) {

}
