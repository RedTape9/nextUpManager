package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

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
