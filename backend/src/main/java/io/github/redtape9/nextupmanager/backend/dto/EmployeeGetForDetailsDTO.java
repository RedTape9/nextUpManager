package io.github.redtape9.nextupmanager.backend.dto;

import lombok.Data;

@Data
public class EmployeeGetForDetailsDTO {
    private String id;
    private String name;
    private String surname;
    private String departmentId;
    private String room;
}
