package io.github.redtape9.nextupmanager.backend.repo;


import io.github.redtape9.nextupmanager.backend.entity.Ticket;
import io.github.redtape9.nextupmanager.backend.entity.TicketStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByDepartmentId(String departmentId);

    List<Ticket> findAllByCurrentStatus(TicketStatus status);

   /* @Query("{'departmentId' : ?0, 'currentStatus' : 'WAITING'}")
    Optional<Ticket> findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(String departmentId);*/

    @Query("{'departmentId' : ?0, 'currentStatus' : 'WAITING'}")
    List<Ticket> findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(String departmentId, Pageable pageable);
}