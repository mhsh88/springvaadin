package ir.sharifi.spring.view.main.component.test.memberWorkTime;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.MemberWorkTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Objects;

@SpringComponent
@UIScope
@Secured({"WORK_TIME_EDIT","WORK_TIME_UPDATE","WORK_TIME_CREATE"})
public class WorkTimeEdit extends VerticalLayout implements KeyNotifier {

    private final MemberWorkTimeRepository repository;
    private final SecurityUserRepository userRepository;

    /**
     * The currently edited memberWorkTime
     */
    private MemberWorkTime memberWorkTime;

    /* Fields to edit properties in Customer entity */
//    "name", "title", "permissions"

    DatePicker  requestDate = new DatePicker("Date");

    TextField hours = new TextField("Hours");
    TextField minutes = new TextField("Minutes");


    HorizontalLayout fields = new HorizontalLayout(requestDate, hours,minutes);

    /* Action buttons */
    // TODO why more code?
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<MemberWorkTime> binder = new Binder<>(MemberWorkTime.class);
    private ChangeHandler changeHandler;

    @Autowired
    public WorkTimeEdit(MemberWorkTimeRepository repository, SecurityUserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;

        add(fields, actions);

        // bind using naming convention
//        binder.forField(permission_name).withNullRepresentation("").bind(Permission::getName,Permission::setName);
//        binder.forField(permission_title).withNullRepresentation("").bind(Permission::getTitle,Permission::setTitle);
        binder.forField(hours).withNullRepresentation("").withConverter(
                new StringToIntegerConverter( Integer.valueOf( 0 ), "integers only" ) )
                .bind( MemberWorkTime::getHours, MemberWorkTime::setHours );
        binder.forField(minutes).withNullRepresentation("").withConverter(
                new StringToIntegerConverter( Integer.valueOf( 0 ), "integers only" ) )
                .bind( MemberWorkTime::getMinutes, MemberWorkTime::setMinutes );

        binder.forField(requestDate).withNullRepresentation(LocalDate.now())
                .withConverter(new Converter<LocalDate, LocalDateTime>() {
                    @Override
                    public Result<LocalDateTime> convertToModel(LocalDate localDate, ValueContext valueContext) {
                        return Result.ok(LocalDateTime.of(Objects.isNull(localDate)?LocalDate.now():localDate, LocalTime.now()));

                    }

                    @Override
                    public LocalDate convertToPresentation(LocalDateTime localDateTime, ValueContext valueContext) {
                        return Objects.isNull(localDateTime)?LocalDate.now():localDateTime.toLocalDate();
                    }
                })
                .bind(MemberWorkTime::getRequestDate,MemberWorkTime::setRequestDate);
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editWorkTime(memberWorkTime));
        setVisible(false);
    }

    void delete() {
        repository.delete(memberWorkTime);
        changeHandler.onChange();
    }

    void save() {
        memberWorkTime.setChangeStatusDate(LocalDateTime.now());
        memberWorkTime.setStatus(WorkTimeType.REQUESTED);
        memberWorkTime.setUser(userRepository.getActiveUser(SecurityUtils.getUsername()));
        if(memberWorkTime.getRequestDate().get(ChronoField.DAY_OF_WEEK)<6) {
            Notification.show(getTranslation("error.work.time.notvalid",getLocale()));

            return;
        }

        repository.save(memberWorkTime);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editWorkTime(MemberWorkTime c) {

        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            memberWorkTime = repository.findById(c.getId()).get();
        }
        else {
            memberWorkTime = c;
        }
        cancel.setVisible(persisted);

        // Bind memberWorkTime properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(memberWorkTime);

        setVisible(true);

        // Focus first name initially
        requestDate.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}
