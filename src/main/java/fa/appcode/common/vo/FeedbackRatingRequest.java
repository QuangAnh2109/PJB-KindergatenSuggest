package fa.appcode.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fa.appcode.common.utils.AuthenticationGet;
import fa.appcode.common.utils.Constant;
import jakarta.validation.constraints.Null;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ToString
@Getter
@NoArgsConstructor
public class FeedbackRatingRequest {
    @JsonProperty(value = "schoolId", required = true)
    private Integer schoolId;

    @JsonProperty(value = "pageNumber")
    private Integer pageNumber = 0;

    @JsonProperty("fromDate")
    private Instant fromDate;

    @JsonProperty("toDate")
    private Instant toDate;

    @JsonProperty("rating")
    private List<Integer> rating = null;

    @Setter
    @JsonIgnore
    private String accountEmail = null;

    @Setter
    @JsonIgnore
    private boolean deleteFlg = false;

    public FeedbackRatingRequest(Integer schoolId) throws NullPointerException{
        this.schoolId = schoolId;
        this.accountEmail = AuthenticationGet.getAccountEmailByAuthen();
    }
}