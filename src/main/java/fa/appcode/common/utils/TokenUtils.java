package fa.appcode.common.utils;

import fa.appcode.KindergartenG3Application;
import fa.appcode.common.vo.AccountVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;

@Component
public class TokenUtils {
    @Autowired
    AccountService accountService;
    public static final String key = "caniloveyou";
    long expirationTime = 3 * 60 * 1000;

    public String generateToken(String email) {
        AccountInfo account = accountService.findByEmail(email);
        if (account != null) {
            long expireAt = Instant.now().toEpochMilli() + expirationTime;
            String token = key + "|" + account.getId() + "|" + expireAt + "|" + account.getPassword() + "|" + account.getDatetimeChangePass();
            System.out.println(token);
            return Base64.getEncoder().encodeToString(token.getBytes());
        }

        return "no generate token";
    }

    public String decodeToken(String token) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(token);
            return new String(decodedBytes);
        } catch (Exception e) {
            return "Invalid token";
        }
    }

    public boolean isTokenExpired(String token) {
        String decodedToken = decodeToken(token);
        String[] parts = decodedToken.split("\\|");
        if (parts.length < 3) {
            return true;
        }
        for(String a : parts) {
            System.out.println(a);
        }
        long expireAt = Long.parseLong(parts[2]);
        return Instant.now().toEpochMilli() > expireAt;
    }
    public int checkIdUserToken(String token) {
        String decodedToken = decodeToken(token);
        String[] parts = decodedToken.split("\\|");
        return Integer.parseInt(parts[1]);
    }
    public Instant getExpiredTime(String token) {
        String decodedToken = decodeToken(token);
        String[] parts = decodedToken.split("\\|");
        return Instant.parse(parts[2]);
    }
    public boolean isTokenValid(String token, String email) {
        AccountInfo account = accountService.findByEmail(email);
        if (account == null) {
            return false;
        }
        String decodedToken = decodeToken(token);
        String[] parts = decodedToken.split("\\|");
        if (parts.length < 5) {
            return false;
        }
        long createdTime = Long.parseLong(parts[2]) - expirationTime;
        long expiredTime = Long.parseLong(parts[2]);
        Instant passwordChangeTime = account.getDatetimeChangePass();

        if (passwordChangeTime != null &&
                passwordChangeTime.toEpochMilli() >= createdTime &&
                passwordChangeTime.toEpochMilli() <= expiredTime) {
            return false;
        }
        return Instant.now().toEpochMilli() <= expiredTime;
    }

}
