package fa.appcode.common.vo;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailVo {
    @NotNull
    private Integer id;

    @NotNull
    private String fullName;

    @NotNull
    private String requestEmail;

    @NotNull
    private String requestPhone;

    @NotNull
    private String address;

    @NotNull
    private String requestSchoolName;

    @NotNull
    private String inquires;

    @NotNull
    private String requestMasterName;
}
