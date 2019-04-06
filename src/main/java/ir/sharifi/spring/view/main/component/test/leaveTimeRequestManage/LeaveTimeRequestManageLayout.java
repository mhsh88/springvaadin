package ir.sharifi.spring.view.main.component.test.leaveTimeRequestManage;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;
import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.service.test.LeaveTimeRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@SpringComponent
@UIScope
@Secured("WORK_TIME_MANAGE")
public class LeaveTimeRequestManageLayout extends VerticalLayout {


    private final LeaveTimeRequestService memberWorkTimeService;

    final Grid<LeaveTimeRequest> grid;

    @Autowired
    public LeaveTimeRequestManageLayout( LeaveTimeRequestService leaveTimeRequestService) {
        this.memberWorkTimeService = leaveTimeRequestService;
        this.grid = new Grid<>(LeaveTimeRequest.class);
        add( grid);

        grid.setHeight("300px");
        grid.setColumns( "amount","status","requestDate", "changeStatusDate");
        grid.addColumn(securityUser -> securityUser.getUser().getFullName())
        .setHeader("User Name");
        grid.addColumn(new ComponentRenderer<>(leaveTimeRequest -> {

            // text field for entering a new name for the person
            Select<String> statusSelection = new Select<String>("ACCEPTED","FAILED");
            statusSelection.setPlaceholder("Click Here To Change Status");
            statusSelection.setValue(leaveTimeRequest.getStatus().toString());
            statusSelection.addValueChangeListener(e->{
                ListDataProvider<LeaveTimeRequest> dataProvider = (ListDataProvider<LeaveTimeRequest>) grid
                        .getDataProvider();

                if(e.getValue().equals("ACCEPTED"))
                    leaveTimeRequestService.changeStatusByManager(leaveTimeRequest, LeaveTimeRequestStatus.ACCEPTED,getLocale());
                else if(e.getValue().equals("FAILED"))
                    leaveTimeRequestService.changeStatusByManager(leaveTimeRequest,LeaveTimeRequestStatus.FAILED,getLocale());

                dataProvider.refreshAll();
                listModels();
            });
            return statusSelection;
        })).setHeader("Status");

        listModels();
    }

    private void listModels() {
        grid.setItems(memberWorkTimeService.findByStatus(LeaveTimeRequestStatus.REQUESTED));
    }

}

