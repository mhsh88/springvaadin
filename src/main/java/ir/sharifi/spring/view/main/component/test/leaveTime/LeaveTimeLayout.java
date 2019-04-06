package ir.sharifi.spring.view.main.component.test.leaveTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.MemberLeaveTime;
import ir.sharifi.spring.repository.security.SecurityUserRepository;
import ir.sharifi.spring.repository.test.MemberLeaveTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.service.test.MemberLeaveTimeService;
import ir.sharifi.spring.view.main.component.test.memberWorkTime.WorkTimeSecurityHelper;
import org.springframework.security.access.annotation.Secured;

import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Secured("LEAVE_TIME_READ")
public class LeaveTimeLayout extends VerticalLayout {


    private final MemberLeaveTimeRepository repo;
    private final MemberLeaveTimeService memberLeaveTimeService;
    private final SecurityUserRepository userRepository;

    final Grid<MemberLeaveTime> grid;

    private final Button addNewBtn;

    public LeaveTimeLayout(MemberLeaveTimeRepository repo, LeaveTimeEdit editor, MemberLeaveTimeService memberLeaveTimeService, SecurityUserRepository userRepository) {
        this.repo = repo;
        this.memberLeaveTimeService = memberLeaveTimeService;
        this.userRepository = userRepository;
        this.grid = new Grid<>(MemberLeaveTime.class);
        this.addNewBtn = new Button("Add New Request", VaadinIcon.PLUS.create());
        add(grid);

        grid.setHeight("300px");
        grid.setColumns( "amount", "status","expirationDate");
        grid.addColumn(leaveTime->leaveTime.getTransactions().stream().map(t->String.valueOf(t.getAmount())).collect(Collectors.joining(",")))
                .setHeader("Transactions");
        grid.addColumn(memberLeaveTime -> memberLeaveTime.getUser().getFullName())
        .setHeader("User Name");


        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.edit(e.getValue());
//            editor.edit(null);
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.edit(new MemberLeaveTime()));

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
            grid.setItems(memberLeaveTimeService.getModels());
        else
            grid.setItems(memberLeaveTimeService.getByUser(SecurityUtils.getUser()));
    }

}

