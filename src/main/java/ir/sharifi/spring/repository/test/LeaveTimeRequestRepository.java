package ir.sharifi.spring.repository.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface LeaveTimeRequestRepository extends JpaRepository<LeaveTimeRequest, UUID> {
    Collection<LeaveTimeRequest> findByUser(SecurityUser user);

    Collection<LeaveTimeRequest> findByStatus(LeaveTimeRequestStatus status);

    List<LeaveTimeRequest> findByUserAndStatus(SecurityUser user, LeaveTimeRequestStatus status);
    List<LeaveTimeRequest> findByUserAndStatusIn(SecurityUser user, List<LeaveTimeRequestStatus> statuses);

    @Query("SELECT r FROM LeaveTimeRequest r WHERE r.user = :user and  r.requestDate >= :start and r.requestDate <= :endDay and r.status <> :status")
    List<LeaveTimeRequest> getRequestByUserAndDateAndNotStatus(
            @Param("user") SecurityUser user,
            @Param("start") LocalDateTime start,
            @Param("endDay") LocalDateTime end,
            @Param("status") LeaveTimeRequestStatus status);

}
