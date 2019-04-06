package ir.sharifi.spring.view.main.component.security.user;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Role;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.repository.security.RoleRepository;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Secured({"USER_CREATE","USER_UPDATE","USER_DELETE"})
public class UserEdit extends VerticalLayout implements KeyNotifier {

    private final SecurityUserRepository repository;
    public final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * The currently edited customer
     */
    private SecurityUser customer;

    /* Fields to edit properties in Customer entity */
    TextField username = new TextField("Username");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    UserRoleSelect roles ;




    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<SecurityUser> binder = new Binder<>(SecurityUser.class);
    private ChangeHandler changeHandler;

    @Autowired
    public UserEdit(SecurityUserRepository repository, RoleRepository roleRepository, BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;

        roles = new UserRoleSelect(roleRepository);
        HorizontalLayout fields = new HorizontalLayout(username, firstName, lastName, email, password, roles);
        add(fields, actions);

        // bind using naming convention
//        binder.forField(roles).withNullRepresentation(null).bind()
        roleSelect(roleRepository.findAll());
//        binder.forField(roles).withNullRepresentation("").withConverter(obj-> {System.out.println(obj);}).bind(Role::getTitle,Role::setTitle)

        binder.forField(roles)
                .asRequired("Please choose the option closest to your profession")
                .withConverter(new MyConverter())
                .bind(SecurityUser::getRoles, SecurityUser::setRoles);
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
    }



    void delete() {
        repository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        customer.setPassword(encoder.encode(customer.getPassword()));
        repository.save(customer);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(SecurityUser c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = repository.findById(c.getId()).get();
        }
        else {
            customer = c;
        }
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // Focus first name initially
        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    private void roleSelect(List<Role> allRoles) {

        Binder<SecurityUser> binder = new Binder<>();


        roles.setLabel("Title");
        roles.setItems(allRoles.stream().map(Role::getTitle).collect(Collectors.toList()));

        roles.setEmptySelectionAllowed(true);
        roles.setEmptySelectionCaption("Select you title");
        roles.addComponents(null, new Hr());

        binder.forField(roles)
                .asRequired("Please choose the option closest to your profession")
                .withConverter(new MyConverter())
                .bind(SecurityUser::getRoles, SecurityUser::setRoles);
    }

}

class MyConverter implements Converter<String, List<Role>> {

    @Override
    public Result<List<Role>> convertToModel(String fieldValue, ValueContext context) {
        // Produces a converted value or an error
        try {

            Role role = ((UserRoleSelect)context.getComponent().get()).getRoleRepository().findByTitle(fieldValue);


            // ok is a static helper method that creates a Result
            return Result.ok(new ArrayList<Role>(){{
                add(role);
            }});
        } catch (NumberFormatException e) {
            // error is a static helper method that creates a Result
            return Result.error("Please enter a number");
        }
    }

    @Override
    public String convertToPresentation(List<Role> roles, ValueContext context) {
        // Converting to the field type should always succeed,
        // so there is no support for returning an error Result.
        return Objects.nonNull(roles)? roles.stream().map(Role::getTitle).collect(Collectors.joining(",")):"";
    }
}
