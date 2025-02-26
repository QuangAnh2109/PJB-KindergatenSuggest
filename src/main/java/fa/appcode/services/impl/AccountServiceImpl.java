package fa.appcode.services.impl;

import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.common.vo.RoleVo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
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

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MasterDataService masterDataService;
    @Autowired
    private AccountRepository accountRepository;
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) {
        return accountRepository.findAllWithFullAddress(search, pageable);
    }

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
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id, Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolByParentId(id, pageable);
    }

    @Override
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolByParentIdAndSchoolOwner(parentId, schoolOwnerId, pageable);
    }

    @Override
    public String findAccountRoleString(String email) {return accountRepository.findAccountRoleString(email);}

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountRepository.getAccountInfoById(id);
    }


    @Override
    public AccountVo findAccountByPhone(String phone) {
        return accountRepository.findByPhone(phone);
    }


}
