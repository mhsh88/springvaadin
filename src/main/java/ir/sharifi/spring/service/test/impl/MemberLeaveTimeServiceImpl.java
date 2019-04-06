package ir.sharifi.spring.service.test.impl;

import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.service.test.MemberLeaveTimeService;
import ir.sharifi.spring.service.test.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberLeaveTimeServiceImpl extends BaseServiceImpl<MemberLeaveTime, UUID> implements MemberLeaveTimeService {

    @Autowired
    private MemberLeaveTimeRepository memberLeaveTimeRepository;

    @Override
    public MemberLeaveTimeRepository getRepository() {
        return memberLeaveTimeRepository;
    }

    @Override
    public List<MemberLeaveTime> getByUser(SecurityUser activeUser) {
        return memberLeaveTimeRepository.findByUser(activeUser);
    }
}
