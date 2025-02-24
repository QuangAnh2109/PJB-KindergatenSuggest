package fa.appcode.services.impl;

import fa.appcode.common.vo.AccountVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDataService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MasterDataService masterDataService;

    @Override
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) {
        Page<AccountInfo> accountPage = accountRepository.findAllWithFullAddress(search, pageable);
        return accountPage.map(this::convertToAccountVo);
    }

    @Override
    public AccountVo getAccountById(Integer id) {
        AccountInfo user = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertToAccountVo(user);
    }

    /**
     * Chuyển đổi AccountInfo thành AccountVo
     */
    private AccountVo convertToAccountVo(AccountInfo accountInfo) {
        AccountVo accountVo = new AccountVo();
        accountVo.setId(accountInfo.getId());
        accountVo.setFullName(accountInfo.getFullName());
        accountVo.setEmail(accountInfo.getEmail());
        accountVo.setPhone(accountInfo.getPhone());
        accountVo.setDob(accountInfo.getDob() != null ? accountInfo.getDob().toString() : null);
        accountVo.setImageUrl(accountInfo.getImageUrl());
        accountVo.setRoleId(accountInfo.getRoleId());

        // Build full address
        if (accountInfo.getAddress() == null && accountInfo.getWard() == null &&
                accountInfo.getDistrict() == null && accountInfo.getCity() == null) {
            accountVo.setFullAddress("No specific information yet");
        } else {
            StringBuilder fullAddress = new StringBuilder(accountInfo.getAddress() != null ? accountInfo.getAddress() : "");
            if (accountInfo.getWard() != null) {
                fullAddress.append(", ").append(accountInfo.getWard().getWardName());
            }
            if (accountInfo.getDistrict() != null) {
                fullAddress.append(", ").append(accountInfo.getDistrict().getDistrictName());
            }
            if (accountInfo.getCity() != null) {
                fullAddress.append(", ").append(accountInfo.getCity().getCityName());
            }
            accountVo.setFullAddress(fullAddress.toString().trim());
        }

        // Resolve role and status names
        accountVo.setRole(masterDataService.getMasterById(accountInfo.getRoleId()));
        accountVo.setStatus(masterDataService.getMasterById(accountInfo.getStatusId()));

        return accountVo;
    }

    @Override
    public void toggleUserStatus(Integer id) {
        AccountInfo user = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Giả sử statusId = 1 là Active, statusId = 2 là Inactive
        if (user.getStatusId() == 41) {
            user.setStatusId(42); // Deactivate
        } else {
            user.setStatusId(41); // Activate
        }

        accountRepository.save(user);
    }

    @Override
    public void updateUser(Integer id, String fullName, String phone, String dob, Integer roleId) {
        AccountInfo user = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Cập nhật các trường được phép chỉnh sửa
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setDob(LocalDate.parse(dob)); // Chuyển đổi String sang LocalDate
        user.setRoleId(roleId);

        accountRepository.save(user);
    }


    @Override
    public Page<AccountInfo> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public List<AccountInfo> findAllRoles() {
        return accountRepository.findAllRole();
    }


    @Override
    public Page<ParentVo> findAllParent(Pageable pageable) {
        return accountRepository.findAllParent(pageable);
    }

    @Override
    public ParentVo findParentById(int id) {
        return accountRepository.findParentById(id);
    }

    @Override
    public Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolBy(id, pageable);
    }
}
