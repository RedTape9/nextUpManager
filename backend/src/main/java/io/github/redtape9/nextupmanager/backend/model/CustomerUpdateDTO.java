package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import java.util.List;
import java.util.Date;

@Data
public class CustomerUpdateDTO {
    private String departmentId;  // optional, falls sie geändert werden soll
    private List<Customers.StatusChange> statusHistory;
    private CustomerStatus currentStatus;
    private String employeeId;
    private String commentByEmployee;

    @Data
    public static class StatusChangeDTO {
        private CustomerStatus status;
        private Date timestamp;
    }
}
