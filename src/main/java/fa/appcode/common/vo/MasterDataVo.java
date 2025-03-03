package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MasterDataVo {
    private Integer id;

    private String typeName;

    private Integer typeKey;

    private String typeValue;
}