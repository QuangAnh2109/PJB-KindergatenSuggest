package fa.appcode.services.impl;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MasterDataService masterDataService;

    @Override
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) {
        Page<AccountInfo> accountPage = accountRepository.findAllWithFullAddress(search, pageable);

        return accountPage.map(accountInfo -> {
            AccountVo accountVo = new AccountVo();
            accountVo.setId(accountInfo.getId());
            accountVo.setFullName(accountInfo.getFullName());
            accountVo.setEmail(accountInfo.getEmail());
            accountVo.setPhone(accountInfo.getPhone());
            accountVo.setDob(accountInfo.getDob() != null ? accountInfo.getDob().toString() : null);

            // Build full address
            String fullAddress = accountInfo.getAddress();
            if (accountInfo.getWard() != null) {
                fullAddress += ", " + accountInfo.getWard().getWardName();
            }
            if (accountInfo.getDistrict() != null) {
                fullAddress += ", " + accountInfo.getDistrict().getDistrictName();
            }
            if (accountInfo.getCity() != null) {
                fullAddress += ", " + accountInfo.getCity().getCityName();
            }
            accountVo.setFullAddress(fullAddress);

            // Resolve role and status names
            String role = masterDataService.getMasterByTypeNameAndTypeKey("ROLE", accountInfo.getRoleId());
            String status = masterDataService.getMasterByTypeNameAndTypeKey("ACCOUNT STATUS", accountInfo.getStatusId());
            accountVo.setRole(role);
            accountVo.setStatus(status);

            return accountVo;
        });
    }
}
