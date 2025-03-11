package fa.appcode.services;

import fa.appcode.common.vo.MyRequestVo;
import fa.appcode.common.vo.RequestDetailVo;
import fa.appcode.common.vo.RequestVo;
import fa.appcode.entities.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RequestService {

    /**
     * Lists all requests with pagination.
     *
     * @param accountID       the account ID for filtering requests
     * @param requestMasterID the request master ID for filtering requests
     * @param pageable        the pagination information
     * @return a page of RequestVo
     */
    Page<RequestVo> listAllRequest(Integer accountID,Integer requestMasterID,Pageable pageable);

    /**
     * Finds a request by its ID.
     *
     * @param id the ID of the request
     * @return the details of the request
     */
    RequestDetailVo findById(Integer id);

    /**
     * Searches for requests based on a keyword, account ID, and request master ID.
     *
     * @param keyword         the search keyword
     * @param accountID       the account ID for filtering requests
     * @param requestMasterID the request master ID for filtering requests
     * @param pageable        the pagination information
     * @return a page of RequestVo
     */
    Page<RequestVo> searchRequest(String keyword, Integer accountID, Integer requestMasterID, Pageable pageable);

    Page<MyRequestVo> findRequestByAccountId(Integer accountId, Pageable pageable);

    /**
     * Updates the status of a request.
     *
     * @param update_id the ID of the user performing the update
     * @param id        the ID of the request to update
     */
    void updateRequest(String update_id, int id);

    /**
     * Sends email reminders for pending requests.
     */
    void emailRequestReminder();

    void createRequest(Request request);
}
