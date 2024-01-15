package io.github.redtape9.nextupmanager.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


@Data
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String departmentId;
    private String customerNr;
    private List<StatusChange> statusHistory = new ArrayList<>();
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
