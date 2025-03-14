package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class SchoolListManager {
    private int schoolId;
    private String schoolName;
    private String schoolAddress;
    private String city;
    private String district;
    private String ward;
    private String schoolPhone;
    private String schoolEmail;
    private Instant postedDate;
    private int statusId;
    private boolean canDelete;
    private int recordNo;
}
