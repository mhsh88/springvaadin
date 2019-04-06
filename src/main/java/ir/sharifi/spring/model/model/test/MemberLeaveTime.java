package ir.sharifi.spring.model.model.test;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.i18n.util.DateUtil;
import ir.sharifi.spring.i18n.util.WorkingMinutesCalculator;
import ir.sharifi.spring.model.converter.MemberLeaveTimeStatusConverter;
import ir.sharifi.spring.model.converter.WorkTimeTypeConverter;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.model.security.SecurityUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Stream;

@Entity
@Table(name = "MEMBER_LEAVE_TIME")
public class MemberLeaveTime extends BaseEntity<UUID> {

    private double amount;
    private Double transientAmount;
    private LocalDateTime expirationDate;
    private MemberLeaveTimeStatus status;
    private LocalDateTime changeStatusDate;
    private List<LeaveTimeRequest> requests;
    private List<LeaveTimeTransaction> transactions;
    private SecurityUser user;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }


    @JsonView
    @ManyToMany
    @JoinTable(
            name="LEAVE_REQUEST"
            , joinColumns={
            @JoinColumn(name="LEAVE_TIME_ID", nullable=false)
    }
            , inverseJoinColumns={
            @JoinColumn(name="REAUEST_ID", nullable=false)
    }
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<LeaveTimeRequest> getRequests() {
        return requests;
    }
    public void setRequests(List<LeaveTimeRequest> requests) {
        this.requests = requests;
    }

    @JsonView
    @Column(name = "AMOUNT")
    @Min(value = 0)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonView
    @Column(name = "EXPIRATION_DATE")
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonView
    @Convert(converter = MemberLeaveTimeStatusConverter.class)
    @Column(name = "status")
    public MemberLeaveTimeStatus getStatus() {
        return status;
    }

    public void setStatus(MemberLeaveTimeStatus status) {
        this.status = status;
    }

    @JsonView
    @Column(name = "CHANGE_STATUS_DATE")
    public LocalDateTime getChangeStatusDate() {
        return changeStatusDate;
    }

    public void setChangeStatusDate(LocalDateTime changeStatusDate) {
        this.changeStatusDate = changeStatusDate;
    }

    @JsonView
    @OneToMany(mappedBy = "leaveTime")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<LeaveTimeTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<LeaveTimeTransaction> transactions) {
        this.transactions = transactions;
    }

    @Transient
    public double getTransientAmount() {
        if(Objects.nonNull(transientAmount))
            return transientAmount;
        return amount;
    }

    @Transient
    public void setTransientAmount(double transientAmount) {
        this.transientAmount = transientAmount;
    }

    @Transient
    public LocalDateTime getBeginningDate(){

        int dayValue = Math.floorDiv(new Double(getTransientAmount()).intValue(), (8 * 60));
        int hourValue = Math.floorDiv(Math.floorMod(new Double(getTransientAmount()).intValue(),8*60),60);
        int minuteValue = Math.floorMod(Math.floorMod(new Double(getTransientAmount()).intValue(),8*60),60);
//        getExpirationDate().mi
        LocalDateTime localDateTime = DateUtil.subtractWorkingMinutes(
                DateUtil.subtractWorkingHour(
                        DateUtil.subtractWorkingDay(getExpirationDate(), dayValue), hourValue), minuteValue);
//        long between = ChronoUnit.MINUTES.between(LocalDateTime.now(), localDateTime);
        LocalDateTime max = Stream.of(LocalDateTime.now(), localDateTime).max(Comparator.naturalOrder()).get();
        return max;

    }
    @Transient
    public double getAmountByBeginningDate(LocalDateTime requestDate) {
        WorkingMinutesCalculator calculator = new WorkingMinutesCalculator();

        if(requestDate.isAfter(getBeginningDate())){
            return Math.min(calculator.getWorkingMinutes(Timestamp.valueOf(requestDate),Timestamp.valueOf(getExpirationDate())),getAmount());
        }
        else{
            return Math.min(calculator.getWorkingMinutes(Timestamp.valueOf(getBeginningDate()),Timestamp.valueOf(getExpirationDate())),getAmount());
        }
    }



}

