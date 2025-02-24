package fa.appcode.services.impl;

import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import fa.appcode.web.controller.ForgotPasswordController;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
//import java.util.logging.Logger;

@Service
public class AccountServiceImpl implements AccountService {

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


}
