package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MasterDataVo {
    private int id;
    private int typeKey;
    private String typeName;
    private String typeValue;
}
