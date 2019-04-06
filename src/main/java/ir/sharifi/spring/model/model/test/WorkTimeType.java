package ir.sharifi.spring.model.model.test;


import ir.sharifi.spring.model.model.BaseEnum;

public enum WorkTimeType implements BaseEnum<String> {
    REQUESTED("R"), ACCEPTED("A"),FAILED("F"),USED("U");

    WorkTimeType(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static WorkTimeType fromValue(String value) {
        for (WorkTimeType assetType : WorkTimeType.values()) {
            if (assetType.getValue().equals(value)) {
                return assetType;
            }
        }
        return null;
    }
}
