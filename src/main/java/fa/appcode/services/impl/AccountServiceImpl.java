package fa.appcode.services.impl;

import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.web.controller.ForgotPasswordController;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import fa.appcode.services.MasterDataService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public AccountInfo findByEmail(String email) {
      return accountRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public boolean updatePassword(String email, String newPassword) {
        logger.info("Bắt đầu cập nhật mật khẩu cho email: {}", email);

        AccountInfo account = accountRepository.findByEmail(email);
        if (account == null) {
            logger.warn("Email không tồn tại: {}", email);
            return false;
        }

        String encodedPassword = "{bcrypt}" + passwordEncoder.encode(newPassword);
        logger.info("Mật khẩu mới đã được mã hóa: {}", encodedPassword);

        int numberOfRows = accountRepository.updatePassword(encodedPassword, email);
        logger.info("Số dòng bị ảnh hưởng bởi câu lệnh UPDATE: {}", numberOfRows);

        return numberOfRows > 0;
    }



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
    public Page<AccountInfo> findAll(Pageable pageable)
            {
        return accountRepository.findAll(pageable);
    }

    @Override
    public List<AccountInfo> findAllRoles(){
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
    public Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id,Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolBy(id,pageable);
    }

    @Override
    public AccountVo findAccountByPhone(String phone) {
        return accountRepository.findByPhone(phone);
    }
}
