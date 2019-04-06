package ir.sharifi.spring.view.main.component.security.role;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.model.model.security.Role;
import ir.sharifi.spring.repository.security.RoleRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;
@SpringComponent
@UIScope
@Secured("ROLE_READ")
public class RoleLayout extends VerticalLayout {


    private final RoleRepository repo;

    final Grid<Role> grid;

    private final Button addNewBtn;

    public RoleLayout(RoleRepository repo, RoleEdit editor) {
        this.repo = repo;
        this.grid = new Grid<>(Role.class);
        this.addNewBtn = new Button("New Role", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout( addNewBtn);
        add(SecurityUtils.isAccessGranted(RoleEdit.class)? actions:new Div(), grid,SecurityUtils.isAccessGranted(RoleEdit.class)?editor:new Div());

        grid.setHeight("300px");
        grid.setColumns( "name", "title");
        grid.addColumn(role -> role.getPermissions().stream().map(Permission::getName).collect(Collectors.joining(",")))
                .setHeader("permissions");
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editRole(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editRole(new Role()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers();
        });

        // Initialize listing
        listCustomers();
    }

    private void listCustomers() {
        grid.setItems(repo.findAll());
    }

}

