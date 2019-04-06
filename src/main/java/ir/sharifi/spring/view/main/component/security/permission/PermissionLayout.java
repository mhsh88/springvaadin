package ir.sharifi.spring.view.main.component.security.permission;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.repository.security.PermissionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;

@SpringComponent
@UIScope
@Secured("PERMISSION_READ")
public class PermissionLayout extends VerticalLayout {


    private final PermissionRepository repo;

    final Grid<Permission> grid;

    private final Button addNewBtn;

    public PermissionLayout(PermissionRepository repo, PermissionEdit permissionEdit) {
        this.repo = repo;
        this.grid = new Grid<>(Permission.class);
        this.addNewBtn = new Button("New Permission", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout( addNewBtn);
        add(grid);
        grid.setHeight("300px");
        grid.setColumns( "name", "title");
        grid.asSingleSelect().addValueChangeListener(e -> {
            permissionEdit.editPermission(e.getValue());
        });

        addNewBtn.addClickListener(e -> permissionEdit.editPermission(new Permission()));

        permissionEdit.setChangeHandler(() -> {
            permissionEdit.setVisible(false);
            listCustomers();
        });

        // Initialize listing
        listCustomers();
    }

    private void listCustomers() {
        grid.setItems(repo.findAll());
    }

}

