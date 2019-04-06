package ir.sharifi.spring.view.main.component.test.leaveTimeRequests;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.security.SecurityUser;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;
import ir.sharifi.spring.model.model.test.LeaveTimeTransaction;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.service.test.LeaveTimeRequestService;
import ir.sharifi.spring.service.test.MemberLeaveTimeService;
import ir.sharifi.spring.view.main.component.test.leaveTime.LeaveTimeEdit;
import ir.sharifi.spring.view.main.component.test.memberWorkTime.WorkTimeSecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Secured("LEAVE_TIME_REQUEST_READ")
public class LeaveTimeRequestLayout extends VerticalLayout {


    private final LeaveTimeRequestService leaveTimeRequestService;

    final Grid<LeaveTimeRequest> grid;

    private final Button addNewBtn;

    @Autowired
    public LeaveTimeRequestLayout(  LeaveTimeRequestService leaveTimeRequestService, LeaveTimeRequestEdit editor) {
        this.leaveTimeRequestService = leaveTimeRequestService;
        this.grid = new Grid<>(LeaveTimeRequest.class);
        this.addNewBtn = new Button("Add New Request", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout( addNewBtn);
        add(actions,grid,editor);

        grid.setHeight("300px");
        grid.setColumns( "requestDate","amount","status","changeStatusDate");
        grid.addColumn(transactions->transactions.getTransactions().stream().map(t->String.valueOf(t.getAmount())).collect(Collectors.joining(",")))
        .setHeader("transactions");
        grid.addColumn(securityUser -> securityUser.getUser().getFullName())
        .setHeader("User Name");
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.edit(e.getValue());
        });

        addNewBtn.addClickListener(e -> editor.edit(new LeaveTimeRequest()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers();
        });

        // Initialize listing
        listCustomers();
    }

    private void listCustomers() {
        if(SecurityUtils.isAccessGranted(WorkTimeSecurityHelper.class))
            grid.setItems(leaveTimeRequestService.getRequestByStatus(LeaveTimeRequestStatus.REQUESTED));
        else
            grid.setItems(leaveTimeRequestService.getUserRequest(SecurityUtils.getUser()));
    }

}



