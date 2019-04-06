package ir.sharifi.spring.view.main.component.test.memberWorkTime;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.repository.test.MemberWorkTimeRepository;
import ir.sharifi.spring.service.security.SecurityUtils;
import ir.sharifi.spring.service.test.MemberWorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@SpringComponent
@UIScope
@Secured("WORK_TIME_READ")
public class WorkTimeLayout extends VerticalLayout {


    private final MemberWorkTimeRepository repo;
    private final WorkTimeEdit editor;
    private final MemberWorkTimeService memberWorkTimeService;
    final Grid<MemberWorkTime> grid;

    private final Button addNewBtn;

    @Autowired
    public WorkTimeLayout(MemberWorkTimeRepository repo, WorkTimeEdit editor, MemberWorkTimeService memberWorkTimeService) {
        this.repo = repo;
        this.editor = editor;
        this.memberWorkTimeService = memberWorkTimeService;
        this.grid = new Grid<>(MemberWorkTime.class);
        this.addNewBtn = new Button("Add New Request", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout( addNewBtn);
        add(SecurityUtils.isAccessGranted(WorkTimeEdit.class)?actions:new Div(), grid,SecurityUtils.isAccessGranted(WorkTimeEdit.class)?editor:new Div());

        grid.setHeight("300px");
        grid.setColumns( "requestDate", "changeStatusDate", "status", "hours","minutes");
        grid.addColumn(securityUser -> securityUser.getUser().getFullName())
        .setHeader("User Name");
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editWorkTime(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editWorkTime(new MemberWorkTime()));

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
            grid.setItems(memberWorkTimeService.getWorkTimeByStatus(WorkTimeType.REQUESTED));
        else
            grid.setItems(memberWorkTimeService.getWorkTimeByUser(SecurityUtils.getUser()));
    }

}

