package ir.sharifi.spring.view.main.component.security.role;

import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.repository.security.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class RolePermissionSelect extends Select<String> {

    private final PermissionRepository permissionRepository;

    @Autowired
    public RolePermissionSelect(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public PermissionRepository getPermissionRepository() {
        return permissionRepository;
    }
}
