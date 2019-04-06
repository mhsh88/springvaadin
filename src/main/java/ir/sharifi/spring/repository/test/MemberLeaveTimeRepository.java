package ir.sharifi.spring.repository.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.model.model.test.MemberLeaveTimeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MemberLeaveTimeRepository extends JpaRepository<MemberLeaveTime, UUID> {
    List<MemberLeaveTime> findByUser(SecurityUser user);
    List<MemberLeaveTime> findByUserAndExpirationDateGreaterThanEqualAndStatus(SecurityUser user, LocalDateTime time, MemberLeaveTimeStatus status);
}
