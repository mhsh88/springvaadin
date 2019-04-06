package ir.sharifi.spring.model.model.test;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.converter.WorkTimeTypeConverter;
import ir.sharifi.spring.model.model.security.SecurityUser;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "MEMBER_WORK_TIME")
public class MemberWorkTime extends BaseEntity<UUID> {
    private LocalDateTime requestDate;
    private LocalDateTime changeStatusDate;
    private WorkTimeType status;
    private int hours;
    private int minutes;
    private SecurityUser user;




    @JsonView
    @Column(name = "REQUEST_DATE")
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
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
    @Convert(converter = WorkTimeTypeConverter.class)
    @Column(name = "status")
    public WorkTimeType getStatus() {
        return status;
    }

    public void setStatus(WorkTimeType status) {
        this.status = status;
    }

    @Column(name = "hours")
    @Min(0)
    @Max(8)
    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Column(name = "minutes")
    @Min(0)
    @Max(59)
    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    public SecurityUser getUser() {
        return user;
    }

    public void setUser(SecurityUser user) {
        this.user = user;
    }
}
