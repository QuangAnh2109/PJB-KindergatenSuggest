package fa.appcode.common.utils;

import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;

@Component
public class TokenUtils {
    @Autowired
    private AccountService accountService;
    private static final String KEY = "gugugu";
    private static final long EXPIRATION_TIME = 10 * 60;

    public String generateToken(String email) throws Exception {
        AccountInfo account = accountService.findByEmail(email);
        if (account == null) {
            throw new Exception("Account not found for email: " + email);
        }
        long expireAt = Instant.now().getEpochSecond() + EXPIRATION_TIME;
        String token = String.join("|", KEY, email, String.valueOf(expireAt), account.getPassword(),
                String.valueOf(account.getDatetimeChangePass()));
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    public String generateTokenRegister(String email) throws Exception {
        String token = String.join("|", KEY, email);
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    public String decodeToken(String token) throws Exception {
        try {
            return new String(Base64.getDecoder().decode(token));
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid token format", e);
        }
    }

    public String getEmailFromToken(String token) throws Exception {
        String[] parts = decodeToken(token).split("\\|");
        if (parts.length < 2) {
            throw new Exception("Invalid token structure");
        }
        return parts[1];
    }

    public long getExpiredTime(String token) throws Exception {
        String[] parts = decodeToken(token).split("\\|");
        if (parts.length < 3) {
            throw new Exception("Invalid token structure");
        }
        return Long.parseLong(parts[2]);
    }

    public long getCreateTime(String token) throws Exception {
        return getExpiredTime(token) - EXPIRATION_TIME;
    }
    public boolean isTokenValid(String token, AccountInfo account) throws Exception {
        long expiredTime = getExpiredTime(token);
        if (Instant.now().getEpochSecond() > expiredTime) {
            return false; // Token expired
        }
        return !isTokenUsed(account, expiredTime);
    }

    public boolean isTokenUsed(AccountInfo account, long expiredTime) throws Exception {
        Instant passwordChangeTime = account.getDatetimeChangePass();
        long createdTime = expiredTime - EXPIRATION_TIME;
        return passwordChangeTime != null && passwordChangeTime.getEpochSecond() >= createdTime;
    }
    public boolean isTokenExpired(String token) throws Exception {
        return Instant.now().getEpochSecond() > getExpiredTime(token);
    }

//    public boolean isTokenUsed(String token, AccountInfo account) throws Exception {
//        Instant passwordChangeTime = account.getDatetimeChangePass();
//        long createdTime = getCreateTime(token);
//        long expiredTime = getExpiredTime(token);
//
//        return passwordChangeTime != null &&
//                passwordChangeTime.getEpochSecond() >= createdTime &&
//                passwordChangeTime.getEpochSecond() <= expiredTime;
//    }
}
