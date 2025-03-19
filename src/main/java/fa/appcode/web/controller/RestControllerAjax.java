package fa.appcode.web.controller;

import fa.appcode.common.utils.Constant;
import fa.appcode.common.vo.FeedbackVo;
import fa.appcode.common.vo.MyRequestVo;
import fa.appcode.common.vo.PageVo;
import fa.appcode.entities.Feedback;
import fa.appcode.services.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequestMapping("/api")
@AllArgsConstructor
@RestController
public class RestControllerAjax {

    private final RequestService requestService;


    @GetMapping("/search-result")
    public String searchResult() {
        return "Search result";
    }

    @GetMapping("/my-request")
    public ResponseEntity<PageVo<MyRequestVo>> myRequest(
            @SessionAttribute(name = "idAccount", required = true) Integer accountId,
            @RequestParam(defaultValue = Constant.INIT_PAGE) int page,
            @RequestParam(defaultValue = Constant.PAGE_SIZE) int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MyRequestVo> requestPage = requestService.findRequestByAccountId(accountId, pageable);

        PageVo<MyRequestVo> pageDto = new PageVo<>();
        pageDto.setContent(requestPage.getContent());
        pageDto.setTotalPages(requestPage.getTotalPages());
        pageDto.setTotalElements(requestPage.getTotalElements());
        pageDto.setCurrentPage(page);

        return ResponseEntity.ok(pageDto);
    }

    @PostMapping("/create-feedback")
    public String createRequest(@SessionAttribute(name = "idAccount",required = true)Integer accountId,
                                @RequestBody FeedbackVo feedbackVo) {
//        Feedback feedback = new Feedback(accountId,feedbackVo,"PARENT",1,1,Instant.now());
        return "Create request";
    }
}
