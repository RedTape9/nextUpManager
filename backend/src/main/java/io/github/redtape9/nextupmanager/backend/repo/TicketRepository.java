package io.github.redtape9.nextupmanager.backend.repo;


import io.github.redtape9.nextupmanager.backend.model.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByDepartmentId(String departmentId);



   /* @Query("{'departmentId' : ?0, 'currentStatus' : 'WAITING'}")
    Optional<Ticket> findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(String departmentId);*/

    @Query("{'departmentId' : ?0, 'currentStatus' : 'WAITING'}")
    List<Ticket> findFirstByDepartmentIdAndCurrentStatusOrderByCreatedAtAsc(String departmentId, Pageable pageable);
}