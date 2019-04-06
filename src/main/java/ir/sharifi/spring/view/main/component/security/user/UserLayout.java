package ir.sharifi.spring.view.main.component.security.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Role;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Secured("USER_READ")
public class UserLayout extends VerticalLayout {


    private final SecurityUserRepository repo;

    final Grid<SecurityUser> grid;

    private final Button addNewBtn;

    public UserLayout(SecurityUserRepository repo, UserEdit editor) {
        this.repo = repo;
        this.grid = new Grid<>(SecurityUser.class);
        this.addNewBtn = new Button("New User", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout( addNewBtn);
        add(actions, grid,editor);

        grid.setHeight("300px");
        grid.setColumns( "firstName", "lastName", "username", "email");
        grid.addColumn(securityUser -> securityUser.getRoles().stream().map(Role::getTitle).collect(Collectors.joining(",")))
        .setHeader("roles");
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new SecurityUser()));

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

