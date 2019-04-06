package ir.sharifi.spring.view.main.component.edit;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import ir.sharifi.spring.model.model.BaseEntity;
import ir.sharifi.spring.service.test.base.BaseService;


public abstract class AbstractEditComponent<T extends BaseEntity> extends VerticalLayout implements KeyNotifier {

    protected final BaseService service;

    /**
     * The currently edited memberWorkTime
     */
    protected T t;




    protected HorizontalLayout fields = new HorizontalLayout();

    /* Action buttons */
    // TODO why more code?
    protected Button save = new Button("Save", VaadinIcon.CHECK.create());
    protected Button cancel = new Button("Cancel");
    protected Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    protected HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    protected Binder<T> binder;
    protected  ChangeHandler changeHandler;


    public AbstractEditComponent(BaseService service) {
        this.service = service;
        binder = new Binder<>(getEntityClass());





        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> edit(t));

    }

    protected void delete() {
        service.delete(t);
        changeHandler.onChange();
    }

    protected void save() {

        service.insert(t);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void edit(T c) {

        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            t = (T) service.findById(c.getId());
        }
        else {
            t = c;
        }
        cancel.setVisible(persisted);

        // Bind memberWorkTime properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(t);

        setVisible(true);

        // Focus first name initially
//        requestDate.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

    public abstract Class<T> getEntityClass();

}
