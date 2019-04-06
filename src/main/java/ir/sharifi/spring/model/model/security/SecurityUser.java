package ir.sharifi.spring.model.model.security;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USER")
public class SecurityUser extends BaseEntity<UUID> {

    private String username;
    private List<Role> roles;
    private SecurityUser manager;
    private List<SecurityUser>  members;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Boolean enabled = true;
    private List<LeaveTimeRequest> leaveTimeRequests;
    private List<MemberLeaveTime> memberLeaveTimes;


    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Transient
    public String getFullName() {
        String fullname = "";
        if (this.getFirstName() != null) {
            fullname = this.getFirstName();
        }
        if (this.getFirstName() != null && this.getLastName() != null) {
            fullname += " ";
        }
        if (this.getLastName() != null) {
            fullname += getLastName();
        }
        return fullname;
    }

    @JsonView
    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @Basic
    @Column(name = "IS_ENABLED")
    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }









    @Basic
    @Column(name = "USER_NAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name="SECURITY_USER_ROLE"
            , joinColumns={
            @JoinColumn(name="SECURITY_USER_ID", nullable=false)
    }
            , inverseJoinColumns={
            @JoinColumn(name="ROLE_ID", nullable=false)
    }
    )
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Transient
    public List<Permission> getPermissions() {
        List<Permission> permissions = new ArrayList<>();
        getRoles().stream().forEach(r -> {
            r.getPermissions().stream().forEach(p -> {
                if (!permissions.contains(p))
                    permissions.add(p);
            });
        });
        return permissions;
    }

    @JsonView
    @ManyToOne( cascade = { CascadeType.ALL } )
    @JoinColumn(name = "MANAGER_ID")
    public SecurityUser getManager() {
        return manager;
    }

    public void setManager(SecurityUser manager) {
        this.manager = manager;
    }
    @JsonView
    @OneToMany(mappedBy = "manager")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<SecurityUser> getMembers() {
        return members;
    }

    public void setMembers(List<SecurityUser> members) {
        this.members = members;
    }

    @JsonView
    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<LeaveTimeRequest> getLeaveTimeRequests() {
        return leaveTimeRequests;
    }

    public void setLeaveTimeRequests(List<LeaveTimeRequest> leaveTimeRequests) {
        this.leaveTimeRequests = leaveTimeRequests;
    }

    @JsonView
    @OneToMany(mappedBy = "user")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<MemberLeaveTime> getMemberLeaveTimes() {
        return memberLeaveTimes;
    }

    public void setMemberLeaveTimes(List<MemberLeaveTime> memberLeaveTimes) {
        this.memberLeaveTimes = memberLeaveTimes;
    }
}
