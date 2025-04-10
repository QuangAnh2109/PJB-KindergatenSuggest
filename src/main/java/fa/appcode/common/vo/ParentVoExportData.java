package fa.appcode.common.vo;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
public class ParentVoExportData {
    @CsvBindByPosition(position = 0)
    private Integer parentId;
    @CsvBindByPosition(position = 1)
    private String parentName;
    @CsvBindByPosition(position = 2)
    private String parentEmail;
    @CsvBindByPosition(position = 3)
    private String parentPhone;
    @CsvBindByPosition(position = 4)
    private String enrollStatus;
    public ParentVoExportData(Integer parentId, String parentName, String parentEmail,String parentPhone, String enrollStatus){
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentEmail = parentEmail;
        this.parentPhone = parentPhone;
        this.enrollStatus = enrollStatus;
    }

    public String getParentPhone() {
        return parentPhone != null ? "\"" + parentPhone + "\"" : "\"\"";
    }
}
