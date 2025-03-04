package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class SchoolListManager {
    private String schoolName;
    private String schoolAddress;
    private String schoolPhone;
    private String schoolEmail;
    private Instant postDate;
    private int statusId;
    private boolean canEdit;
    private boolean canDelete;
}
