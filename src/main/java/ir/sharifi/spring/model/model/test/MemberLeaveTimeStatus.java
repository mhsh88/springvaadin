package ir.sharifi.spring.model.model.test;

import ir.sharifi.spring.model.model.BaseEnum;


public enum MemberLeaveTimeStatus implements BaseEnum<String> {
    SUBMIT("S"),USED("U");

    MemberLeaveTimeStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static MemberLeaveTimeStatus fromValue(String value) {
        for (MemberLeaveTimeStatus assetType : MemberLeaveTimeStatus.values()) {
            if (assetType.getValue().equals(value)) {
                return assetType;
            }
        }
        return null;
    }
}
