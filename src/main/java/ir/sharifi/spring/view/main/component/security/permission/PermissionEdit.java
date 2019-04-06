package ir.sharifi.spring.view.main.component.security.permission;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.Permission;
import ir.sharifi.spring.repository.security.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@SpringComponent
@UIScope
@Secured({"PERMISSION_EDIT","PERMISSION_UPDATE","PERMISSION_CREATE"})
public class PermissionEdit extends VerticalLayout implements KeyNotifier {

    private final PermissionRepository repository;

    /**
     * The currently edited permission
     */
    private Permission permission;

    /* Fields to edit properties in Customer entity */
//    "name", "title", "permissions"
    TextField permission_name = new TextField("Permission Name");
    TextField permission_title = new TextField("Permission Title");


    HorizontalLayout fields = new HorizontalLayout(permission_name, permission_title);

    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Permission> binder = new Binder<>(Permission.class);
    private ChangeHandler changeHandler;

    @Autowired
    public PermissionEdit(PermissionRepository repository) {
        this.repository = repository;

        add(fields, actions);

        // bind using naming convention
        binder.forField(permission_name).withNullRepresentation("").bind(Permission::getName,Permission::setName);
        binder.forField(permission_title).withNullRepresentation("").bind(Permission::getTitle,Permission::setTitle);
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editPermission(permission));
        setVisible(false);
    }

    void delete() {
        repository.delete(permission);
        changeHandler.onChange();
    }

    void save() {
        repository.save(permission);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editPermission(Permission c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            permission = repository.findById(c.getId()).get();
        }
        else {
            permission = c;
        }
        cancel.setVisible(persisted);

        // Bind permission properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(permission);

        setVisible(true);

        // Focus first name initially
        permission_title.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}
