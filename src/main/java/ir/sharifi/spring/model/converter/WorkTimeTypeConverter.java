package ir.sharifi.spring.model.converter;

import ir.sharifi.spring.model.model.test.WorkTimeType;

import javax.persistence.AttributeConverter;


public class WorkTimeTypeConverter implements AttributeConverter<WorkTimeType, Character> {
    @Override
    public Character convertToDatabaseColumn(WorkTimeType workTimeType) {
        if (workTimeType != null) {
            return workTimeType.getValue().charAt(0);
        }
        return null;
    }

    @Override
    public WorkTimeType convertToEntityAttribute(Character s) {
        if (s != null) {
            return WorkTimeType.fromValue(s.toString());
        }
        return null;
    }
}
