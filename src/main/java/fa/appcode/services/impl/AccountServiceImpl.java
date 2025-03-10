package fa.appcode.services.impl;

import fa.appcode.common.logging.Log4jUtils;
import fa.appcode.common.utils.Constant;

import fa.appcode.common.utils.Placeholder;
import fa.appcode.common.utils.SendMailInfo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.exceptions.EntityNotFoundException;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.services.EmailService;
import fa.appcode.services.MasterDatumService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private MasterDatumService masterDatumService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;


    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AccountInfo getAccountById(int id) {
        return accountRepository.getAccountInfoById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.findByEmail(email) != null;
    }

    @Override
    public AccountInfo findAccountInfoByPhone(String phone) {
        return accountRepository.findAccountByPhone(phone);
    }

    @Override
    public String encodePassword(String password) {
        return "{bcrypt}" + passwordEncoder.encode(password);
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
        String encodedPassword = encodePassword(newPassword);
        account.setPassword(encodedPassword);
        account.setUpdateTime(Instant.now());
        account.setDatetimeChangePass(Instant.now());
        account.setRecordNo(account.getRecordNo() + 1);
        accountRepository.save(account);
        return true;
    }

    @Override
    public AccountInfo createAccount(AccountVo accountVo) {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPassword(encodePassword(accountVo.getPassword()));
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setStatusId(Constant.STATUS_INACTIVE);
        accountInfo.setRoleId(Constant.PARENT_ROLE_ID);
        accountInfo.setImageUrl(null);
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId(Constant.WEB_SYSTEM);
        accountInfo.setUpdateId(Constant.WEB_SYSTEM);
        Instant now = Instant.now();
        accountInfo.setCreateTime(now);
        accountInfo.setUpdateTime(now);
        return accountRepository.save(accountInfo);
    }

    // Get list of user account
    @Override
    public Page<AccountVo> getAllAccounts(String search, Pageable pageable) throws Exception {
        return accountRepository.findAllWithFullAddress(search, pageable);
    }

    // Find account by Id
    @Override
    public AccountVo getAccountById(Integer id) {
        AccountInfo accountInfo = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

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
        accountVo.setRole(masterDatumService.getMasterByTypeNameAndTypeKey("ROLE", accountInfo.getRoleId()));
        accountVo.setStatus(masterDatumService.getMasterByTypeNameAndTypeKey("ACCOUNT STATUS", accountInfo.getStatusId()));
        accountVo.setRecordNo(accountInfo.getRecordNo());
        return accountVo;
    }


    // Update user account
    @Override
    public int updateAccount(AccountVo accountVo) {
        AccountInfo user = accountRepository.findById(accountVo.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Log4jUtils.getLogger().info("recordNo get in DB : {}", user.getRecordNo());

        if (!user.getRecordNo().equals(accountVo.getRecordNo())) {
            throw new IllegalStateException("Data has been modified by someone else!"); // Xử lý lỗi ở Service
        }
        // Update role or status of account
        user.setRoleId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        user.setStatusId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus()));

        int newRecordNo = user.getRecordNo() + 1;
        user.setRecordNo(newRecordNo);
        user.setUpdateId("SYSTEM_ADMIN");
        user.setUpdateTime(Instant.now());

        accountRepository.save(user);

        return newRecordNo;
    }


    // Delete logic user account
    @Override
    public void deleteAccount(Integer id) {
        AccountInfo account = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        account.setDeleteFlg(true);
        accountRepository.save(account);
    }

    @Override
    public void addUserFromAdmin(AccountVo accountVo, Principal principal) {
        // Generate password by system
        String randomPassword = UUID.randomUUID().toString();
        accountVo.setPassword(randomPassword);
        accountVo.setConfirmPassword(randomPassword);

        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setFullName(accountVo.getFullName());
        accountInfo.setEmail(accountVo.getEmail());
        accountInfo.setPhone(accountVo.getPhone());
        accountInfo.setDob(LocalDate.parse(accountVo.getDob()));
        accountInfo.setRoleId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ROLE", accountVo.getRole()));
        accountInfo.setPassword(encodePassword(accountVo.getPassword()));
        accountInfo.setStatusId(masterDatumService.getMasterKeyByTypeNameAndTypeValue("ACCOUNT STATUS", accountVo.getStatus())); // Default status
        accountInfo.setImageUrl("null");
        accountInfo.setRecordNo(1);
        accountInfo.setCreateId("SYSTEM_ADMIN");
        accountInfo.setUpdateId("SYSTEM_ADMIN");
        accountInfo.setCreateTime(Instant.now());
        accountInfo.setUpdateTime(Instant.now());
        accountRepository.save(accountInfo);

        // Send mail
        emailService.sendEmailToMany(SendMailInfo.builder()
                .toMail(List.of(accountVo.getEmail()))
                .ccMail(List.of())
                .mailId(2)
                .detail(Map.of(Placeholder.USER_NAME, accountVo.getEmail(),
                        Placeholder.EMAIL, accountVo.getEmail(),
                        Placeholder.PASSWORD, randomPassword,
                        Placeholder.OWNER_ACCOUNT, this.getAccountInfo(principal).getFullName()))
                .build());
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

    @Transactional
    @Override
    public void updateAccountInfo(AccountInfo existing, AccountInfo formData) {
        existing.setFullName(formData.getFullName());
        existing.setPhone(formData.getPhone());
        existing.setDob(formData.getDob());
        existing.setUpdateTime(Instant.now());
        existing.setCity(formData.getCity());
        existing.setDistrict(formData.getDistrict());
        existing.setWard(formData.getWard());
        existing.setAddress(formData.getAddress());
        existing.setRecordNo(existing.getRecordNo() + 1);
        accountRepository.save(existing);
    }

    //    @Override
//    public AccountInfo findWithFullAddressByEmail(String email, boolean deleteFlg) {
//        return accountRepository.findWithFullAddressByEmail(email, deleteFlg);
//    }
    @Override
    public String getEmailByAccountIdAndActiveAndNoDelete(int accountId) {
        return accountRepository.getEmailByAccountIdAndStatusIdAndDeleteFlg(accountId, 1, false);
    }

    @Override
    public String getSchoolOwnerEmailBySchoolIdAndActiveAndNoDelete(int id) {
        return accountRepository.getSchoolOwnerEmailBySchoolIdAndStatusAndDeleteFlg(id, 1, false);
    }
}
