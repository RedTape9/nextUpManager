package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;

public class DepartmentUpdateDTO {
    @Data
    public static class Create {
        private int currentNumber;
    }
}
