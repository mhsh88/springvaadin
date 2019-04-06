package ir.sharifi.spring.view.main.component.test.memberWorkTimeManage;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import ir.sharifi.spring.model.model.test.MemberWorkTime;
import ir.sharifi.spring.model.model.test.WorkTimeType;
import ir.sharifi.spring.repository.test.MemberWorkTimeRepository;
import ir.sharifi.spring.service.test.MemberWorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;

@SpringComponent
@UIScope
@Secured("WORK_TIME_MANAGE")
public class WorkTimeManageLayout extends VerticalLayout {


    private final MemberWorkTimeRepository repo;
    private final MemberWorkTimeService memberWorkTimeService;

    final Grid<MemberWorkTime> grid;

    @Autowired
    public WorkTimeManageLayout(MemberWorkTimeRepository repo, MemberWorkTimeService memberWorkTimeService) {
        this.repo = repo;
        this.memberWorkTimeService = memberWorkTimeService;
        this.grid = new Grid<>(MemberWorkTime.class);
        add( grid);

        grid.setHeight("300px");
        grid.setColumns( "requestDate", "changeStatusDate", "hours","minutes");
        grid.addColumn(securityUser -> securityUser.getUser().getFullName())
        .setHeader("User Name");
        grid.addColumn(new ComponentRenderer<>(memberWorkTime -> {

            // text field for entering a new name for the person
            Select<String> statusSelection = new Select<String>("ACCEPTED","FAILED");
            statusSelection.setPlaceholder("Click Here To Change Status");
            statusSelection.setValue(memberWorkTime.getStatus().toString());
            statusSelection.addValueChangeListener(e->{
                ListDataProvider<MemberWorkTime> dataProvider = (ListDataProvider<MemberWorkTime>) grid
                        .getDataProvider();

                if(e.getValue().equals("ACCEPTED"))
                    memberWorkTimeService.changeStatusByManager(memberWorkTime,WorkTimeType.ACCEPTED,getLocale());
                else if(e.getValue().equals("FAILED"))
                    memberWorkTimeService.changeStatusByManager(memberWorkTime,WorkTimeType.FAILED,getLocale());

                dataProvider.refreshAll();
                listCustomers();
            });
            return statusSelection;
        })).setHeader("Status");
        listCustomers();
    }

    private void listCustomers() {
        grid.setItems(memberWorkTimeService.findByStatus(WorkTimeType.REQUESTED));
    }

}

