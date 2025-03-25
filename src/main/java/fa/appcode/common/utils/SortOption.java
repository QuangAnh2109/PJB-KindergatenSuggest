package fa.appcode.common.utils;

import org.springframework.data.domain.Sort;

public enum SortOption {
    BY_RATING("By Rating", "rating", Sort.Direction.DESC),
    NEWEST_TO_OLDEST("Newest to Oldest", "createdDate", Sort.Direction.DESC),
    MONTHLY_FEE_HIGHEST("Monthly fee(Highest)", "monthlyFee", Sort.Direction.DESC),
    MONTHLY_FEE_LOWEST("Monthly fee(Lowest)", "monthlyFee", Sort.Direction.ASC);

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