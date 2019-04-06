package ir.sharifi.spring.repository.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MemberWorkTimeRepository extends JpaRepository<MemberWorkTime, UUID> {
    List<MemberWorkTime> findByStatus(WorkTimeType status);

    Collection<MemberWorkTime> findByUser(SecurityUser user);
}
