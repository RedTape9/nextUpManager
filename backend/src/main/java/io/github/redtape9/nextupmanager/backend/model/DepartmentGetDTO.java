package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;

@Data
public class DepartmentGetDTO {
    private String id;
    private String name;
    private String prefix;
    private int currentNumber;
}
