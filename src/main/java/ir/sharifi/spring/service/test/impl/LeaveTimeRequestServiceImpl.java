package ir.sharifi.spring.service.test.impl;

import com.vaadin.flow.component.notification.Notification;
import ir.sharifi.spring.exception.LeaveTimeRequestAmountException;
import ir.sharifi.spring.exception.LeaveTimeRequestNotValidException;
import ir.sharifi.spring.i18n.util.DateUtil;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.*;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.LeaveTimeRequestRepository;
import ir.sharifi.spring.repository.test.LeaveTimeTransactionRepository;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.service.test.LeaveTimeRequestService;
import ir.sharifi.spring.service.test.base.BaseServiceImpl;
import org.dom4j.IllegalAddException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LeaveTimeRequestServiceImpl extends BaseServiceImpl<LeaveTimeRequest, UUID> implements LeaveTimeRequestService {

    @Autowired
    private LeaveTimeRequestRepository leaveTimeRequestRepository;
    @Autowired
    private SecurityUserRepository userRepository;

    @Autowired
    private MemberLeaveTimeRepository memberLeaveTimeRepository;

    @Autowired
    private LeaveTimeTransactionRepository leaveTimeTransactionRepository;

    @Override
    public LeaveTimeRequestRepository getRepository() {
        return leaveTimeRequestRepository;
    }

    @Override
    @Transactional
    public LeaveTimeRequest createLeaveTimeRequest(LeaveTimeRequest request) {

        if ((!DateUtil.isWorkingDay(request.getRequestDate()) || !DateUtil.isWorkingHour(request.getRequestDate()))) {
            Notification.show("Request Time is not Working Day or Working Hour");
            throw new LeaveTimeRequestNotValidException("Request Time is not Working Day or Working Hour");
        }


        if (request.getRequestDate().isBefore(LocalDateTime.now())) {
            Notification.show("Request Time is not valid");
            throw new LeaveTimeRequestNotValidException("Request Time is not valid");
        }

        List<LeaveTimeRequest> allrequests = leaveTimeRequestRepository.findByUserAndStatusIn(SecurityUtils.getUser(), Arrays.asList(LeaveTimeRequestStatus.REQUESTED, LeaveTimeRequestStatus.ACCEPTED));

        defineRequestInterference(request, allrequests);


        List<LeaveTimeRequest> requestByDate = leaveTimeRequestRepository.getRequestByUserAndDateAndNotStatus(SecurityUtils.getUser(), request.getRequestDate().toLocalDate().atStartOfDay(), request.getRequestDate().toLocalDate().atStartOfDay().plusDays(1), LeaveTimeRequestStatus.FAILED);
        defineRequestInterference(request, requestByDate);
        if (requestByDate.size() > 2) {
            Notification.show("You can not add more then 2 requests in a same day");
            throw new LeaveTimeRequestNotValidException("You can not add more then 2 requests in a same day");
        }


        List<LeaveTimeRequest> requests = leaveTimeRequestRepository.findByUserAndStatus(SecurityUtils.getUser(), LeaveTimeRequestStatus.REQUESTED);
        requests.add(request);

        Set<MemberLeaveTime> leaveTimeSet = requests.stream().map(rqst -> memberLeaveTimeRepository.findByUserAndExpirationDateGreaterThanEqualAndStatus(SecurityUtils.getUser(), rqst.getRequestDate(), MemberLeaveTimeStatus.SUBMIT)).flatMap(Collection::stream).collect(Collectors.toSet());
        if (requests.stream().mapToDouble(LeaveTimeRequest::getAmountCompareNow).sum() >

                leaveTimeSet.stream()
                        .mapToDouble(memberLeaveTime ->
                                memberLeaveTime.getAmountByBeginningDate(
                                        requests.stream().map(LeaveTimeRequest::getRequestDate).min(
                                                Comparator.naturalOrder()
                                        ).get()
                                )).sum()) {
            Notification.show("Request Amount is more than Requested Leave Times.");
            throw new LeaveTimeRequestAmountException("Request Amount is more than Requested Leave Times.");

        }
        if (request.getAmount() > memberLeaveTimeRepository.findByUserAndExpirationDateGreaterThanEqualAndStatus(SecurityUtils.getUser(), request.getRequestDate(), MemberLeaveTimeStatus.SUBMIT)
                .stream().mapToDouble(memberLeaveTime -> memberLeaveTime.getAmountByBeginningDate(request.getRequestDate())).sum()) {

            Notification.show("Request Amount is more than Leave Time.");
            throw new LeaveTimeRequestAmountException("Request Amount is more than Leave Time");

        }
        request.setChangeStatusDate(LocalDateTime.now());
        request.setStatus(LeaveTimeRequestStatus.REQUESTED);
        request.setUser(userRepository.getActiveUser(SecurityUtils.getUsername()));
        return leaveTimeRequestRepository.save(request);
    }

    private void defineRequestInterference(LeaveTimeRequest request, List<LeaveTimeRequest> requestByDate) {

        if (requestByDate.stream()
                .anyMatch(leaveTimeRequest ->
                        (request.getRequestDate().isEqual(leaveTimeRequest.getRequestDate()) || request.getEnding().isEqual(leaveTimeRequest.getEnding())) ||
                                (request.getRequestDate().isAfter(leaveTimeRequest.getRequestDate()) && request.getRequestDate().isBefore(leaveTimeRequest.getEnding()) ||
                                        request.getEnding().isAfter(leaveTimeRequest.getRequestDate()) && request.getEnding().isBefore(leaveTimeRequest.getEnding())))) {
            Notification.show("Request Interference! you have already request in this time");
            throw new LeaveTimeRequestNotValidException("Request Interference! you have already request in this time");
        }
    }

    @Override
    public Collection<LeaveTimeRequest> getUserRequest(SecurityUser user) {
        return leaveTimeRequestRepository.findByUser(user);
    }

    @Override
    public Collection<LeaveTimeRequest> getRequestByStatus(LeaveTimeRequestStatus status) {
        return leaveTimeRequestRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public void changeStatusByManager(LeaveTimeRequest leaveTimeRequest, LeaveTimeRequestStatus status, Locale locale) {
        final Double[] amount = {leaveTimeRequest.getAmountCompareNow()};
        leaveTimeRequest.setStatus(status);
        leaveTimeRequest.setChangeStatusDate(LocalDateTime.now());
        insert(leaveTimeRequest);
        if (status.equals(LeaveTimeRequestStatus.ACCEPTED)) {


            List<MemberLeaveTime> leaveTimes = memberLeaveTimeRepository.findByUserAndExpirationDateGreaterThanEqualAndStatus(leaveTimeRequest.getUser(), leaveTimeRequest.getRequestDate(), MemberLeaveTimeStatus.SUBMIT);
            leaveTimes.sort(Comparator.comparing(MemberLeaveTime::getExpirationDate));
            double sum = leaveTimes.stream().map(memberLeaveTime -> {
                double amountByBeginningDate = memberLeaveTime.getAmountByBeginningDate(leaveTimeRequest.getChangeStatusDate());
                if (amount[0] == 0) {
                    return memberLeaveTime;
                }
                LeaveTimeTransaction transaction = new LeaveTimeTransaction();

                if (amount[0] >= amountByBeginningDate) {

                    transaction.setUser(leaveTimeRequest.getUser());
                    transaction.setLeaveTime(memberLeaveTime);
                    transaction.setAmount(-amountByBeginningDate);
                    transaction.setTransactionTime(LocalDateTime.now());
                    transaction.setRequest(leaveTimeRequest);


                    memberLeaveTime.setStatus(MemberLeaveTimeStatus.USED);
                    memberLeaveTime.setAmount(memberLeaveTime.getAmount() - amountByBeginningDate);
                    memberLeaveTime.setChangeStatusDate(LocalDateTime.now());
                    amount[0] = amount[0] - amountByBeginningDate;

                } else {
                    transaction.setUser(leaveTimeRequest.getUser());
                    transaction.setLeaveTime(memberLeaveTime);
                    transaction.setAmount(-amount[0]);
                    transaction.setTransactionTime(LocalDateTime.now());
                    transaction.setRequest(leaveTimeRequest);
                    memberLeaveTime.setAmount(memberLeaveTime.getAmount() - amount[0]);
                    amount[0] = 0.0;

                }
                leaveTimeTransactionRepository.save(transaction);
                memberLeaveTime = memberLeaveTimeRepository.save(memberLeaveTime);
                return memberLeaveTime;
            }).mapToDouble(MemberLeaveTime::getAmount).sum();
            if(sum<0){
                Notification.show("Amount of request not accepted");
                throw new LeaveTimeRequestAmountException("Amount of request not accepted");
            }
        }


    }



    @Override
    public Collection<LeaveTimeRequest> findByStatus(LeaveTimeRequestStatus status) {
        return leaveTimeRequestRepository.findByStatus(status);
    }
}
