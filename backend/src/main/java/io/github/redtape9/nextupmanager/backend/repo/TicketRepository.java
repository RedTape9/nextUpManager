package io.github.redtape9.nextupmanager.backend.repo;

import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findAllByCurrentStatus(TicketStatus status);

    Optional<Ticket> findTopByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(String departmentId, TicketStatus currentStatus);

    boolean existsByEmployeeIdAndCurrentStatus(String employeeId, TicketStatus status);

    Optional<Ticket> findByEmployeeIdAndCurrentStatus(String employeeId, TicketStatus currentStatus);


}