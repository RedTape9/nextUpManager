package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketUpdateDTO {
    private String status;
    private String commentByEmployee;
    private String timestamp;
    private List<StatusChangeDTO> statusHistory;
}
