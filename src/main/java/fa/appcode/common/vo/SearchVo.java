package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchVo {
    private int school_id;
    private String keyword;
    private String type;
    private String childAge;
    private int cityId;
    private int districtId;
    private double fee_from;


}
