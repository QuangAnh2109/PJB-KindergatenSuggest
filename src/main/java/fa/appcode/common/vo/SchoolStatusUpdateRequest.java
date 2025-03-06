package fa.appcode.common.vo;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

@Builder
@Getter
public class SchoolStatusUpdateRequest {
    @Min(1)
    private int id;

    @Column(columnDefinition = "VARCHAR(255)")
    @Email
    private String email;

    @Min(1)
    private int recordNo;

    @Min(1)
    private int schoolStatus;

    @NotEmpty
    private List<@NotEmpty @Min(1) Integer> statusList;

    @Column(columnDefinition = "VARCHAR(255)")
    @NotEmpty
    private String updateId;

    @NotNull
    private Instant updateTime;

    private boolean deleteFlg;
}
