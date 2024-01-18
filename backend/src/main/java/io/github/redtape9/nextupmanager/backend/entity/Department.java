package io.github.redtape9.nextupmanager.backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "departments")
public class Department {
    @Id
    private String id;
    private String name;
    private String prefix;
    private int currentNumber;
}