package ir.sharifi.spring.repository.security;

import ir.sharifi.spring.model.model.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Permission findByName(String name);

    Permission findByTitle(String title);
}
