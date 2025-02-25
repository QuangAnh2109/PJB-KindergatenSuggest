package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class CityVo implements Serializable {
    private Integer id;
    private String cityName;
}
