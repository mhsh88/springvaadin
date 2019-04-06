package ir.sharifi.spring.model.converter;

import ir.sharifi.spring.model.model.test.MemberLeaveTimeStatus;

import javax.persistence.AttributeConverter;


public class MemberLeaveTimeStatusConverter implements AttributeConverter<MemberLeaveTimeStatus, Character> {
    @Override
    public Character convertToDatabaseColumn(MemberLeaveTimeStatus memberLeaveTimeStatus) {
        if (memberLeaveTimeStatus != null) {
            return memberLeaveTimeStatus.getValue().charAt(0);
        }
        return null;
    }

    @Override
    public MemberLeaveTimeStatus convertToEntityAttribute(Character s) {
        if (s != null) {
            return MemberLeaveTimeStatus.fromValue(s.toString());
        }
        return null;
    }
}
