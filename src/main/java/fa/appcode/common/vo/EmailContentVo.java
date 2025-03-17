package fa.appcode.common.vo;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Getter
@Setter
public class EmailContentVo {
    private Integer id;
    private String email;
    private int roleID;
    private Long numberOfRequest;
}
