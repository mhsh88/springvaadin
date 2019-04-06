package ir.sharifi.spring.service.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.service.test.base.BaseService;

import java.util.List;
import java.util.UUID;

public interface MemberLeaveTimeService extends BaseService<MemberLeaveTime, UUID> {
    List<MemberLeaveTime> getByUser(SecurityUser activeUser);
}
