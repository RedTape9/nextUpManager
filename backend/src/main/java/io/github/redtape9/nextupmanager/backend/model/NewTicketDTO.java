package io.github.redtape9.nextupmanager.backend.model;

import java.time.LocalDateTime;

public record NewTicketDTO(

        String departmentId,
        String customerId,
        String currentNumber,
        LocalDateTime createdAt,
        TicketStatus status

) { }
