package ir.sharifi.spring.view.main.component.test.leaveTimeRequests;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.service.test.LeaveTimeRequestService;
import ir.sharifi.spring.view.main.component.edit.AbstractEditComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@SpringComponent
@UIScope
@Secured({"LEAVE_TIME_REQUEST_UPDATE","LEAVE_TIME_REQUEST_CREATE","LEAVE_TIME_REQUEST_DELETE"})
public class LeaveTimeRequestEdit extends AbstractEditComponent<LeaveTimeRequest> {

    private final LeaveTimeRequestService leaveTimeRequestService;

//    TextField amount = new TextField("Amount");
    LeaveTimeAmountUI amount = new LeaveTimeAmountUI();


    @Autowired
    public LeaveTimeRequestEdit(LeaveTimeRequestService leaveTimeRequestService) {
        super(leaveTimeRequestService);
        this.leaveTimeRequestService = leaveTimeRequestService;


        fields.add(amount);

        add(fields, actions);

        // bind using naming convention
//        binder.forField(permission_name).withNullRepresentation("").bind(Permission::getName,Permission::setName);
//        binder.forField(permission_title).withNullRepresentation("").bind(Permission::getTitle,Permission::setTitle);
        binder.forField(amount).withNullRepresentation(new LeaveTimeRequest())
                .bind( LeaveTimeRequest::getSelf, LeaveTimeRequest::setSelf );
//        binder.forField(amount).withNullRepresentation("").withConverter(
//                new StringToIntegerConverter( Integer.valueOf( 0 ), "integers only" ) )
//                .bind( LeaveTimeRequest::getAmount, LeaveTimeRequest::setAmount );


        binder.bindInstanceFields(this);

        setVisible(false);

    }

    @Override
    protected void save() {
        leaveTimeRequestService.createLeaveTimeRequest(t);
        changeHandler.onChange();
    }

    public void edit(LeaveTimeRequest c) {

        super.edit(c);
        amount.focus();
    }


    @Override
    public Class<LeaveTimeRequest> getEntityClass() {
        return LeaveTimeRequest.class;
    }


}
