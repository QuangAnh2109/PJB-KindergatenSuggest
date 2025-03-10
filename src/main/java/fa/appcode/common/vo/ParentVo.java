package fa.appcode.common.vo;

import fa.appcode.entities.SchoolInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentVo {
    private Integer parentId;
    private String parentName;
    private String parentEmail;
    private String parentPhone;
    private LocalDate DOB;
    private String parentAddress;
    private String enrollStatus;
    private List<SchoolInfo> enrolledSchoolName;
    private Float latestRating;
    private String latestFeedback;

    public ParentVo(Integer parentId, String parentName, String parentEmail,String parentPhone, String enrollStatus){
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentPhone = parentPhone;
        this.enrollStatus = enrollStatus;
    }
    public ParentVo(Integer parentId, String parentName, String parentEmail, String parentPhone, LocalDate DOB, String parentAddress){
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentPhone = parentPhone;
        this.DOB = DOB;
        this.parentAddress = parentAddress;
    }

    public String getParentAddress() {
        if(parentAddress!=null) {
            return this.parentAddress.replace("   ", " - ");
        }
        else {
            return null;
        }
    }
}
