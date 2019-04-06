package ir.sharifi.spring.model.model.security;

import com.fasterxml.jackson.annotation.JsonView;
import ir.sharifi.spring.model.model.BaseEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "SEC_PERMISSION")
public class Permission extends BaseEntity<UUID> {

    private String name;
    private String title;
    private List<Role> roles;

    public Permission() {
    }

    public Permission(String name, String title) {
        this.name = name;
        this.title = title;
    }


    @Basic
    @Column(name = "ENGLISH_TITLE")
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
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(title, that.title) &&
                Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, title, roles);
    }
}
