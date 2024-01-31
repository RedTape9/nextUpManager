package io.github.redtape9.nextupmanager.backend.entity;

import java.time.LocalDateTime;

public record StatusChange(TicketStatus status, LocalDateTime timestamp) {
}
