package io.github.redtape9.nextupmanager.backend.dto;

import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data

public class StatusChangeDTO {
    private TicketStatus status;
    private LocalDateTime timestamp;
}
