package ir.sharifi.spring.model.converter;

import ir.sharifi.spring.model.model.test.LeaveTimeRequestStatus;

import javax.persistence.AttributeConverter;


public class LeaveTimeRequestConverter implements AttributeConverter<LeaveTimeRequestStatus, Character> {
    @Override
    public Character convertToDatabaseColumn(LeaveTimeRequestStatus leaveTimeRequestStatus) {
        if (leaveTimeRequestStatus != null) {
            return leaveTimeRequestStatus.getValue().charAt(0);
        }
        return null;
    }

    @Override
    public LeaveTimeRequestStatus convertToEntityAttribute(Character s) {
        if (s != null) {
            return LeaveTimeRequestStatus.fromValue(s.toString());
        }
        return null;
    }
}
