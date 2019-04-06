package ir.sharifi.spring.view.main.component.security.user;

import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class UserRoleSelect extends Select<String> {

    private final RoleRepository roleRepository;

    @Autowired
    public UserRoleSelect(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
}
