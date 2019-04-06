package ir.sharifi.spring.view.main.component.security.role;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.model.model.security.Role;
import ir.sharifi.spring.repository.security.PermissionRepository;
import ir.sharifi.spring.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Secured({"ROLE_EDIT","ROLE_UPDATE","ROLE_CREATE"})
public class RoleEdit extends VerticalLayout implements KeyNotifier {

    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;

    /**
     * The currently edited role
     */
    private Role role;

    /* Fields to edit properties in Customer entity */
//    "name", "title", "permissions"
    TextField roleName = new TextField("Role Name");
    TextField roleTitle = new TextField("Role Title");
    RolePermissionSelect rolePermissionSelect;


    HorizontalLayout fields = new HorizontalLayout(roleName, roleTitle);

    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Role> binder = new Binder<>(Role.class);
    private ChangeHandler changeHandler;

    @Autowired
    public RoleEdit(RoleRepository repository, PermissionRepository permissionRepository) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
        rolePermissionSelect = new RolePermissionSelect(permissionRepository);



        // bind using naming convention
        binder.forField(roleName).withNullRepresentation("").bind(Role::getName,Role::setName);
        binder.forField(roleTitle).withNullRepresentation("").bind(Role::getTitle,Role::setTitle);
        permissionSelect(permissionRepository.findAll());
//        binder.forField(roles).withNullRepresentation("").withConverter(obj-> {System.out.println(obj);}).bind(Role::getTitle,Role::setTitle)

        binder.forField(rolePermissionSelect)
                .asRequired("Please choose the option closest to your profession")
                .withConverter(new PermissionConverter())
                .bind(Role::getPermissions, Role::setPermissions);
        binder.bindInstanceFields(this);

        fields.add(rolePermissionSelect);
        add(fields, actions);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editRole(role));
        setVisible(false);
    }

    private void permissionSelect(List<Permission> all) {
        rolePermissionSelect.setLabel("Title");
        rolePermissionSelect.setItems(all.stream().map(Permission::getTitle).collect(Collectors.toList()));

        rolePermissionSelect.setEmptySelectionAllowed(true);
        rolePermissionSelect.setEmptySelectionCaption("Select permission");
        rolePermissionSelect.addComponents(null, new Hr());

    }

    void delete() {
        repository.delete(role);
        changeHandler.onChange();
    }

    void save() {
        repository.save(role);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editRole(Role c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            role = repository.findById(c.getId()).get();
        }
        else {
            role = c;
        }
        cancel.setVisible(persisted);

        // Bind role properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(role);

        setVisible(true);

        // Focus first name initially
        roleTitle.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}
class PermissionConverter implements Converter<String, List<Permission>> {

    @Override
    public Result<List<Permission>> convertToModel(String fieldValue, ValueContext context) {
        // Produces a converted value or an error
        try {

            Permission permission = ((RolePermissionSelect)context.getComponent().get()).getPermissionRepository().findByTitle(fieldValue);


            // ok is a static helper method that creates a Result
            return Result.ok(new ArrayList<Permission>(){{
                add(permission);
            }});
        } catch (NumberFormatException e) {
            // error is a static helper method that creates a Result
            return Result.error("Please enter a number");
        }
    }

    @Override
    public String convertToPresentation(List<Permission> model, ValueContext context) {
        // Converting to the field type should always succeed,
        // so there is no support for returning an error Result.
        return model.stream().map(Permission::getTitle).collect(Collectors.joining(","));
    }
}