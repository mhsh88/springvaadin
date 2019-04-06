package ir.sharifi.spring.model.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class BaseEntity<ID extends Serializable> implements Serializable {

    private ID id;
    private Boolean deleted = false;

    @Id
    @Column(name = "ID",columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Column(name = "IS_DELETED")
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean isDeleted) {
        this.deleted = isDeleted;
    }

    public void logicallyDelete() {
        this.deleted = true;
    }

    public void logicallyRetrieve() {
        this.deleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        if(Objects.isNull(id))
            return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}