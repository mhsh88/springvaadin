package ir.sharifi.spring.service.test;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.service.test.base.BaseService;

import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

public interface LeaveTimeRequestService extends BaseService<LeaveTimeRequest, UUID> {
    LeaveTimeRequest createLeaveTimeRequest(LeaveTimeRequest request);

    Collection<LeaveTimeRequest> getUserRequest(SecurityUser user);

    Collection<LeaveTimeRequest> getRequestByStatus(LeaveTimeRequestStatus status);

    void changeStatusByManager(LeaveTimeRequest leaveTimeRequest, LeaveTimeRequestStatus status, Locale locale);

    Collection<LeaveTimeRequest> findByStatus(LeaveTimeRequestStatus status);
}
