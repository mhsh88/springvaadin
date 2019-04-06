package ir.sharifi.spring.model.model.security;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.model.model.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "SEC_ROLE")
public class Role extends BaseEntity<UUID> {

    private String name;
    private String title;
    private List<SecurityUser> securityUsers;
    private List<Permission> permissions;

    @Basic
    @Column(name = "ENGLISH_NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonView
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    public List<SecurityUser> getOperators() {
        return securityUsers;
    }
    public void setOperators(List<SecurityUser> securityUsers) {
        this.securityUsers = securityUsers;
    }

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(
            name = "SEC_ROLE_PERMISSION",
            joinColumns = {@JoinColumn(name = "role_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "permission_id", nullable = false)}
    )
    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name) &&
                Objects.equals(title, role.title) &&
                Objects.equals(securityUsers, role.securityUsers) &&
                Objects.equals(permissions, role.permissions);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, title, securityUsers, permissions);
    }
}
