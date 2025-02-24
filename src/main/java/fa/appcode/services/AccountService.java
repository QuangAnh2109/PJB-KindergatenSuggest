package fa.appcode.services;

import fa.appcode.entities.AccountInfo;



public interface AccountService {
 boolean existsByEmail(String email);
 String encodePassword(String password);
 void save(AccountInfo accountInfo);
 AccountInfo findByEmail(String email);
 public boolean updatePassword(String email, String newPassword);
}

