package fa.appcode.common.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageVo<E> {
    private List<?> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;

}
