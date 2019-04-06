package ir.sharifi.spring.model.model.test;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.model.security.SecurityUser;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "LEAVE_TIME_TRANSACTION")
public class LeaveTimeTransaction extends BaseEntity<UUID> {

    private LocalDateTime transactionTime;
    private LeaveTimeRequest request;
    private double amount;
    private SecurityUser user;
    private MemberLeaveTime leaveTime;

    @JsonView
    @Column(name = "TRANSACTION_TIME")
    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    @JsonView
    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    public LeaveTimeRequest getRequest() {
        return request;
    }

    public void setRequest(LeaveTimeRequest request) {
        this.request = request;
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
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }

    @JsonView
    @ManyToOne
    @JoinColumn(name = "LEAVE_TIME_ID")
    public MemberLeaveTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(MemberLeaveTime leaveTime) {
        this.leaveTime = leaveTime;
    }
}
