package fa.appcode.common.utils;

import org.springframework.data.domain.Sort;

public enum SortOption {
    BY_RATING_DESC("Highest Rating", "avgRating", Sort.Direction.DESC),
    NEWEST_TO_OLDEST("Newest to Oldest", "createdDate", Sort.Direction.DESC),
    TUITION_FEE_DESC("Tuition Fee (Highest)", "feeFrom", Sort.Direction.DESC),
    TUITION_FEE_ASC("Tuition Fee (Lowest)", "feeFrom", Sort.Direction.ASC),
    NAME_ASC("School Name (A-Z)", "schoolName", Sort.Direction.ASC),
    NAME_DESC("School Name (Z-A)", "schoolName", Sort.Direction.DESC);

    private final String displayName;
    private final String fieldName;
    private final Sort.Direction direction;

    SortOption(String displayName, String fieldName, Sort.Direction direction) {
        this.displayName = displayName;
        this.fieldName = fieldName;
        this.direction = direction;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public Sort toSort() {
        return Sort.by(direction, fieldName);
    }
}