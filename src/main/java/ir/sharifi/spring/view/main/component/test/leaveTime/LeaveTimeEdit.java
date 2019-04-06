package ir.sharifi.spring.view.main.component.test.leaveTime;

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
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.repository.test.MemberWorkTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.service.test.MemberLeaveTimeService;
import ir.sharifi.spring.view.main.component.edit.AbstractEditComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

@SpringComponent
@UIScope
@Secured({"LEAVE_TIME_EDIT","LEAVE_TIME_UPDATE","LEAVE_TIME_CREATE"})
public class LeaveTimeEdit extends AbstractEditComponent<MemberLeaveTime> {

//"amount", "expirationDate", "requests", "transactions"
    TextField amount = new TextField("Amount");
    DatePicker expirationDate = new DatePicker("Expiration Date");



    @Autowired
    public LeaveTimeEdit(MemberLeaveTimeService memberLeaveTimeService) {
        super(memberLeaveTimeService);
        amount.setEnabled(false);
        expirationDate.setEnabled(false);
        fields.add(amount,expirationDate);

//        addWorkingDay(fields, actions);

        // bind using naming convention
//        binder.forField(permission_name).withNullRepresentation("").bind(Permission::getName,Permission::setName);
//        binder.forField(permission_title).withNullRepresentation("").bind(Permission::getTitle,Permission::setTitle);
        binder.forField(amount).withNullRepresentation("").withConverter(
                new StringToDoubleConverter( Double.valueOf( 0 ), "numbers only" ) )
                .bind( MemberLeaveTime::getAmount, MemberLeaveTime::setAmount );
//        binder.forField(minutes).withNullRepresentation("").withConverter(
//                new StringToIntegerConverter( Integer.valueOf( 0 ), "integers only" ) )
//                .bind( MemberWorkTime::getMinutes, MemberWorkTime::setMinutes );
        binder.forField(expirationDate).withNullRepresentation(null)
                .withConverter(new Converter<LocalDate, LocalDateTime>() {
                    @Override
                    public Result<LocalDateTime> convertToModel(LocalDate localDate, ValueContext valueContext) {
                        return Result.ok(LocalDateTime.of(localDate, LocalTime.now()));

                    }

                    @Override
                    public LocalDate convertToPresentation(LocalDateTime localDateTime, ValueContext valueContext) {
                        return localDateTime.toLocalDate();
                    }
                })
                .bind(MemberLeaveTime::getExpirationDate,MemberLeaveTime::setExpirationDate);
        binder.bindInstanceFields(this);


    }




    public void edit(MemberLeaveTime c) {

        super.edit(c);
        amount.focus();
    }

    @Override
    public Class<MemberLeaveTime> getEntityClass() {
        return MemberLeaveTime.class;
    }

}
