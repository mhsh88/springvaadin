package ir.sharifi.spring.service.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.service.test.base.BaseService;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface MemberWorkTimeService extends BaseService<MemberWorkTime, UUID> {
    List<MemberWorkTime> findByStatus(WorkTimeType workTimeType);

    void changeStatusByManager(MemberWorkTime memberWorkTime, WorkTimeType workTimeType, Locale locale);

    Collection<MemberWorkTime> getWorkTimeByUser(SecurityUser user);

    Collection<MemberWorkTime> getWorkTimeByStatus(WorkTimeType type);
}
