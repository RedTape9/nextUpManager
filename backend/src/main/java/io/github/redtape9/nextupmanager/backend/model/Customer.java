package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;


@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String departmentId;
    private String customerNr;
    private List<StatusChange> statusHistory;
    private CustomerStatus currentStatus;
    private String createdAt;
    private String employeeId;
    private String room;
    private String commentByEmployee;
    @Data
    public static class StatusChange {
        private CustomerStatus status;
        private String timestamp;
    }

}
