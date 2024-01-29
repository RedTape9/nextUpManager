package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class StatusChangeDTO {
    private String status;
    private LocalDateTime timestamp;
}
