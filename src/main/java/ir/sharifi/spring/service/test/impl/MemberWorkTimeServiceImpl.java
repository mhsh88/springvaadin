package ir.sharifi.spring.service.test.impl;

import ir.sharifi.spring.i18n.util.DateUtil;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.*;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.LeaveTimeTransactionRepository;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.repository.test.MemberWorkTimeRepository;
import ir.sharifi.spring.service.test.MemberWorkTimeService;
import ir.sharifi.spring.service.test.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class MemberWorkTimeServiceImpl extends BaseServiceImpl<MemberWorkTime, UUID> implements MemberWorkTimeService {


    private final MemberWorkTimeRepository workTimeRepository;
    private final MemberLeaveTimeRepository memberLeaveTimeRepository;
    private final SecurityUserRepository userRepository;
    private final LeaveTimeTransactionRepository transactionRepository;

    @Autowired
    public MemberWorkTimeServiceImpl(MemberWorkTimeRepository workTimeRepository, MemberLeaveTimeRepository memberLeaveTimeRepository, SecurityUserRepository userRepository, LeaveTimeTransactionRepository transactionRepository) {
        this.workTimeRepository = workTimeRepository;
        this.memberLeaveTimeRepository = memberLeaveTimeRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public MemberWorkTimeRepository getRepository() {
        return workTimeRepository;
    }

    @Override
    public List<MemberWorkTime> findByStatus(WorkTimeType workTimeType) {
        return workTimeRepository.findByStatus(workTimeType);
    }

    @Override
    @Transactional
    public void changeStatusByManager(MemberWorkTime memberWorkTime, WorkTimeType workTimeType, Locale locale) {

        memberWorkTime.setStatus(workTimeType);
        memberWorkTime.setChangeStatusDate(LocalDateTime.now());
        memberWorkTime = insert(memberWorkTime);
        if(workTimeType.equals(WorkTimeType.ACCEPTED) && memberWorkTime.getRequestDate().get(ChronoField.DAY_OF_WEEK)>5){
//            WeekFields weekFields = WeekFields.of(locale);
//            int dayOfWeek = memberWorkTime.getRequestDate().get(weekFields.dayOfWeek());
//            boolean isItWeekendNow = LocalDateTime.now().query(new IsWeekendQuery());
//            int weekNumber = memberWorkTime.getRequestDate().get(weekFields.weekOfWeekBasedYear());
            MemberLeaveTime memberLeaveTime = new MemberLeaveTime();
            memberLeaveTime.setUser(memberWorkTime.getUser());
            memberLeaveTime.setStatus(MemberLeaveTimeStatus.SUBMIT);
            memberLeaveTime.setChangeStatusDate(LocalDateTime.now());
            if(memberWorkTime.getRequestDate().get(ChronoField.DAY_OF_WEEK) == 6){


                memberLeaveTime.setAmount((memberWorkTime.getHours()*60 + memberWorkTime.getMinutes())*1.4);

            }else if(memberWorkTime.getRequestDate().get(ChronoField.DAY_OF_WEEK) == 7){
                memberLeaveTime.setAmount((memberWorkTime.getHours()*60 + memberWorkTime.getMinutes())*2.0);

            }
            memberLeaveTime.setExpirationDate(DateUtil.addWorkingDay(memberWorkTime.getRequestDate(),21));
            memberLeaveTime = memberLeaveTimeRepository.save(memberLeaveTime);

            LeaveTimeTransaction transaction = new LeaveTimeTransaction();
            transaction.setAmount(memberLeaveTime.getAmount());
            transaction.setLeaveTime(memberLeaveTime);
            transaction.setTransactionTime(LocalDateTime.now());
            transaction.setUser(memberWorkTime.getUser());
            transactionRepository.save(transaction);
        }


    }

    @Override
    public Collection<MemberWorkTime> getWorkTimeByUser(SecurityUser user) {
        return workTimeRepository.findByUser(user);
    }

    @Override
    public Collection<MemberWorkTime> getWorkTimeByStatus(WorkTimeType type) {
        return workTimeRepository.findByStatus(type);
    }
}
