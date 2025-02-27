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
    private Long numberOfRequest;
}
