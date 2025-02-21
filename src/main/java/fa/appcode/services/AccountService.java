package fa.appcode.services;

import fa.appcode.common.vo.AccountVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    Page<AccountVo> getAllAccounts(String search, Pageable pageable);
}
