package io.github.redtape9.nextupmanager.backend.dto;

import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import lombok.Data;

import java.util.List;
@Data
public class TicketUpdateDTO {
    private String commentByEmployee;
    private TicketStatus currentStatus;
    private List<StatusChangeDTO> statusHistory;
}

