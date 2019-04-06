package ir.sharifi.spring.view.main.component.test.leaveTimeRequests;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.timepicker.TimePicker;
import ir.sharifi.spring.model.model.test.LeaveTimeRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class LeaveTimeAmountUI extends CustomField<LeaveTimeRequest> {

    DatePicker requestDate = new DatePicker("Request Date");
    TimePicker timePicker = new TimePicker("Request Time");
    NumberField dayField = new NumberField("Day");
    NumberField hourField = new NumberField("Hours");
    NumberField minuteField = new NumberField("Minutes");
    HorizontalLayout horizontalLayout;

    private LeaveTimeRequest request;


    public LeaveTimeAmountUI() {
        hourField.setMin(0);
        hourField.setMax(8);
        hourField.setStep(1);
        minuteField.setMin(0);
        minuteField.setMax(59);
        minuteField.setStep(1);

        timePicker.setMin("08:00");
        timePicker.setMax("16:00");


        timePicker.setStep(Duration.ofMinutes(30));


        horizontalLayout = new HorizontalLayout(requestDate,timePicker,dayField,hourField,minuteField);

        add(horizontalLayout);
    }


    @Override
    protected LeaveTimeRequest generateModelValue() {

        request = new LeaveTimeRequest();
        request.setRequestDate(LocalDateTime.of(requestDate.getValue(),timePicker.getValue()));
        request.setAmount(dayField.getValue()*8.0*60.0 + hourField.getValue()*60.0 + minuteField.getValue());
        return request;
    }

    @Override
    protected void setPresentationValue(LeaveTimeRequest leaveTimeRequest) {
        double dayValue = (double) Math.floorDiv(new Double(leaveTimeRequest.getAmount()).intValue(), (8 * 60));
        double hourValue = (double) Math.floorDiv(Math.floorMod(new Double(leaveTimeRequest.getAmount()).intValue(),8*60),60);
        double minuteValue = (double) Math.floorMod(Math.floorMod(new Double(leaveTimeRequest.getAmount()).intValue(),8*60),60);
        LocalDateTime requestDate = leaveTimeRequest.getRequestDate();
        this.requestDate.setValue(Objects.nonNull(requestDate)?requestDate.toLocalDate():null);
        this.timePicker.setValue(Objects.nonNull(requestDate)?requestDate.toLocalTime():null);

        dayField.setValue(dayValue);
        hourField.setValue(hourValue);
        minuteField.setValue(minuteValue);



    }
}
