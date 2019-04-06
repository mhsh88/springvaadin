package ir.sharifi.spring.repository.security;

import ir.sharifi.spring.model.model.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByTitle(String title);
}
