package ir.sharifi.spring.repository.security;

import ir.sharifi.spring.model.model.security.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SecurityUserRepository extends JpaRepository<SecurityUser, UUID> {
    @Query("select o from SecurityUser o where o.username = :username and o.enabled = true and o.deleted = false")
    SecurityUser getActiveUser(@Param("username") String username);

}