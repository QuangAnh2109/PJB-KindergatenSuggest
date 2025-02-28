package fa.appcode.common.enums;

public enum UserStatus {
    ACTIVE(1), INACTIVE(2);

    private final int statusId;

    UserStatus(int value) {
        this.statusId = value;
    }

    public int getStatusId() {
        return statusId;
    }

    public static UserStatus fromStatusId(int value) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getStatusId() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
