package fa.appcode.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

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
