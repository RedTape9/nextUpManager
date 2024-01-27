package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

import java.util.List;
@Data
public class TicketUpdateDTO {
    private String commentByEmployee;
    private String currentStatus;
    private List<StatusChangeDTO> statusHistory;
}

