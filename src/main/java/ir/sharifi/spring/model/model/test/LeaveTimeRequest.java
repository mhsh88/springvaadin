package ir.sharifi.spring.model.model.test;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.i18n.util.DateUtil;
import ir.sharifi.spring.i18n.util.WorkingMinutesCalculator;
import ir.sharifi.spring.model.converter.LeaveTimeRequestConverter;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.model.security.SecurityUser;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Entity
@Table(name = "LEAVE_TIME_REQUEST")
public class LeaveTimeRequest extends BaseEntity<UUID> {
    private LocalDateTime requestDate;
    private double amount;
    private LeaveTimeRequestStatus status;
    private LocalDateTime changeStatusDate;
    private SecurityUser user;
    private List<LeaveTimeTransaction> transactions;
    private List<MemberLeaveTime> leaveTimes;

    @JsonView
    @Column(name = "REQUEST_DATE")
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    @JsonView
    @Column(name = "AMOUNT")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @JsonView
    @Convert(converter = LeaveTimeRequestConverter.class)
    @Column(name = "STATUS")
    public LeaveTimeRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveTimeRequestStatus status) {
        this.status = status;
    }

    @JsonView
    @Column(name = "CHANGE_STATUS_TIME")
    public LocalDateTime getChangeStatusDate() {
        return changeStatusDate;
    }

    public void setChangeStatusDate(LocalDateTime changeStatusDate) {
        this.changeStatusDate = changeStatusDate;
    }

    @JsonView
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }

    @JsonView
    @OneToMany(mappedBy = "request")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<LeaveTimeTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<LeaveTimeTransaction> transactions) {
        this.transactions = transactions;
    }

    @JsonView
    @ManyToMany(mappedBy = "requests")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<MemberLeaveTime> getLeaveTimes() {
        return leaveTimes;
    }

    public void setLeaveTimes(List<MemberLeaveTime> leaveTimes) {
        this.leaveTimes = leaveTimes;
    }
    @Transient
    public LeaveTimeRequest getSelf(){
        return this;
    }
    @Transient
    public void setSelf(LeaveTimeRequest request){
        setAmount(request.getAmount());
        setRequestDate(request.getRequestDate());
        setStatus(request.getStatus());
        setChangeStatusDate(request.getChangeStatusDate());
        setUser(request.getUser());
        setLeaveTimes(request.getLeaveTimes());
        setTransactions(request.getTransactions());
        setDeleted(request.getDeleted());
        setId(request.getId());

    }
    @Transient
    public LocalDateTime getEnding(){
        int dayValue = Math.floorDiv(new Double(getAmount()).intValue(), (8 * 60));
        int hourValue = Math.floorDiv(Math.floorMod(new Double(getAmount()).intValue(),8*60),60);
        int minuteValue = Math.floorMod(Math.floorMod(new Double(getAmount()).intValue(),8*60),60);
//        getExpirationDate().mi
        return DateUtil.addWorkingMinutes(
                DateUtil.addWorkingHour(
                        DateUtil.addWorkingDay(getRequestDate(),dayValue),hourValue),minuteValue);
    }

    @Transient
    public double getAmountCompareNow(){
        WorkingMinutesCalculator calculator = new WorkingMinutesCalculator();
        LocalDateTime max = Stream.of(requestDate, LocalDateTime.now()).max(Comparator.comparing(localDateTime1 -> localDateTime1.atZone(ZoneId.systemDefault()).toEpochSecond())).get();


        return Math.min(calculator.getWorkingMinutes(Timestamp.valueOf(max),Timestamp.valueOf(getEnding())),getAmount());
    }
}
