package fa.appcode.services.impl;

import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.common.vo.RoleVo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.services.MasterDatumService;
import fa.appcode.web.controller.ForgotPasswordController;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fa.appcode.services.MasterDataService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MasterDatumService masterDatumService;
    @Autowired
    private AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AccountInfo getAccountById(int id) {
        return accountRepository.getAccountInfoById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email) != null;
    }

    @Transactional
    @Modifying
    public void updateAccountInfo(AccountInfo accountInfo) {
        accountRepository.save(accountInfo);
    }

    @Override
    public AccountInfo findAccountInfoByPhone(String phone) {
        return accountRepository.findAccountByPhone(phone);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Transactional
    @Override
    public void save(AccountInfo accountInfo) {
        accountRepository.save(accountInfo);
    }

    @Override
    public AccountVo findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    @Override
    public AccountInfo findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    @Transactional
    @Override
    public boolean updatePassword(String email, String newPassword) {

        AccountInfo account = accountRepository.findByEmail(email);
        if (account == null) {
            return false;
        }
        String encodedPassword = "{bcrypt}" + passwordEncoder.encode(newPassword);
        int numberOfRows = accountRepository.updatePassword(encodedPassword, email);
        return numberOfRows > 0;
    }
    @Override
    public AccountInfo createAccount(AccountVo accountVo) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPassword("{bcrypt}" + passwordEncoder.encode(accountVo.getPassword()));
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setStatusId(0);
        accountInfo.setRoleId(3);
        accountInfo.setImageUrl("null");
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("WEB_SYSTEM");
        accountInfo.setUpdateId("WEB_SYSTEM");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        return accountRepository.save(accountInfo); // Lưu vào DB
    }

    // Get list of user account
    @Override
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) {
        return accountRepository.findAllWithFullAddress(search, pageable);
    }

    // Find account by Id
    @Override
    public AccountVo getAccountById(Integer id) {
        AccountInfo user = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return convertToAccountVo(user);
    }

    // ========================================================
    @Override
    public Page<AccountInfo> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<AccountInfo> findAllRoles() {
        return List.of();
    }



    @Override
    public Page<ParentVo> findAllParent(Pageable pageable) {
        return null;
    }

    //=========================================================
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
        accountVo.setRole(masterDatumService.getMasterById(accountInfo.getRoleId()));
        accountVo.setStatus(masterDatumService.getMasterById(accountInfo.getStatusId()));
        return accountVo;
    }


    // Change status from active to inactive (and vice versa)
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

    // Update user account by information get from form
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

    // Delete logic user account
    public void deleteAccount(Integer id) {
        AccountInfo account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        account.setDeleteFlg(true);
        accountRepository.save(account);
    }

    public Page<ParentVo> findAllParent(String search, Pageable pageable) {
        return accountRepository.findAllParent(search, pageable);
    }

    @Override
    public ParentVo findParentById(int id) {
        return accountRepository.findParentById(id);
    }

    @Override
    public Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id, Pageable pageable) {
        return null;
    }


    @Override
    public String findAccountRoleString(String email) {
        return accountRepository.findAccountRoleString(email);
    }

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountRepository.getAccountInfoById(id);
    }


    @Override
    public AccountVo findAccountByPhone(String phone) {
        return accountRepository.findByPhone(phone);
    }

    @Override
    public Page<ParentVo> findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(String email, String search, Pageable pageable) {
        return accountRepository.findAllParentIfParentEnrollToSchoolOwnerOrNotByEmail(email, search, pageable);
    }

    @Override
    public AccountInfo getAccountInfo(Principal principal) {
        String user = principal.getName();
        AccountInfo account = findByEmail(user);
        return account;
    }


}
