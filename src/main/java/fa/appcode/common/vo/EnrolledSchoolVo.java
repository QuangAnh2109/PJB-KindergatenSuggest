package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EnrolledSchoolVo {
    private Integer enrollId;
    private String enrolledName;
    private Float latestRate;
    private String latestFeedback;

    public EnrolledSchoolVo(Integer enrollId,String enrolledName){
        this.enrollId = enrollId;
        this.enrolledName = enrolledName;
    }
    @Override
    public String toString() {
        return "EnrolledSchoolVo{" +
                "enrollId=" + enrollId +
                ", enrolledName='" + enrolledName + '\'' +
                ", latestRate=" + latestRate +
                ", latestFeedback='" + latestFeedback + '\'' +
                '}';
    }
}
