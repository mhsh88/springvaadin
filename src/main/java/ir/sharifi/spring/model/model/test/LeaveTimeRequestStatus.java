package ir.sharifi.spring.model.model.test;

import ir.sharifi.spring.model.model.BaseEnum;


public enum LeaveTimeRequestStatus implements BaseEnum<String> {
    REQUESTED("R"), ACCEPTED("A"),FAILED("F"),USED("U");

    LeaveTimeRequestStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static LeaveTimeRequestStatus fromValue(String value) {
        for (LeaveTimeRequestStatus assetType : LeaveTimeRequestStatus.values()) {
            if (assetType.getValue().equals(value)) {
                return assetType;
            }
        }
        return null;
    }
}
