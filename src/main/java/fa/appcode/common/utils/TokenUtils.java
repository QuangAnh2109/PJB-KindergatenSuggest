package fa.appcode.common.utils;

import fa.appcode.entities.AccountInfo;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {
    @Autowired
    AccountService accountService;
    public static final String key = "BAINAYKHOQUA";
    private static final Map<String, String> tokenStore = new HashMap<>();
    private static final Map<String, Long> tokenExpireTime = new HashMap<>();
    long expirationTime = 10 * 60;

//    public String generateToken(String email) {
//        AccountInfo account = accountService.findByEmail(email);
//        if (account != null) {
//            long expireAt = Instant.now().getEpochSecond() + expirationTime;
//            String token = key + "|" + account.getId() + "|" + expireAt + "|" + account.getPassword() + "|" + account.getDatetimeChangePass();
//            System.out.println(token);
//            return Base64.getEncoder().encodeToString(token.getBytes());
//        }
//        return "no generate token";
//    }

    public String generateTokenRegister(String email) {
        long expireAt = Instant.now().getEpochSecond() + expirationTime;
        String token = key + "|" + email + "|" + expireAt;
        System.out.println("token in generateTokenRegister: " + token);
        return Base64.getEncoder().encodeToString(token.getBytes());
    }
//public String getEmailFromToken(String token) {
//        try{
//            String decodedToken = decodeToken(token);
//            String[] parts = decodedToken.split("\\|");
//            return parts[1];
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return "can not generate token";
//}

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
        for (String a : parts) {
            System.out.println(a);
        }
        long expireAt = Long.parseLong(parts[2]);
        return Instant.now().getEpochSecond() > expireAt;
    }

    public int checkIdUserToken(String token) {
        String decodedToken = decodeToken(token);
        String[] parts = decodedToken.split("\\|");
        return Integer.parseInt(parts[1]);
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
                passwordChangeTime.getEpochSecond() >= createdTime &&
                passwordChangeTime.getEpochSecond() <= expiredTime) {
            return false;
        }
        return Instant.now().getEpochSecond() <= expiredTime;
    }

    //    public boolean isTokenValid1(String token, String email) {
//        AccountInfo account = accountService.findByEmail(email);
//        if (account == null) {
//            return false;
//        }
//        String decodedToken = decodeToken(token);
//        if (decodedToken != null) {
//            String[] parts = decodedToken.split("\\|");
//            if (parts.length >= 5) {
//                long expireAt = Long.parseLong(parts[2]);
//                String tokenPassword = parts[3];
//                String tokenPasswordChangeTime = parts[4];
//                if (Instant.now().toEpochMilli() <= expireAt) {
//                    return account.getPassword().equals(tokenPassword) &&
//                            account.getDatetimeChangePass().toString().equals(tokenPasswordChangeTime);
//                }
//            }
//        }
//        return false;
//    }
    public String generateTokenReset(String email) {
        AccountInfo account = accountService.findByEmail(email);
        if (account != null) {
            long expireAt = Instant.now().getEpochSecond() + expirationTime;
            String token = key + "|" + account.getId() + "|" + expireAt + "|" + account.getPassword() + "|" + account.getDatetimeChangePass();
            String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());
            tokenStore.put(email, encodedToken);
            tokenExpireTime.put(email, expireAt);
            System.out.println("generateToken" + encodedToken);
            return encodedToken;
        }
        return "no generate token";
    }

    public String getExistingTokenIfValid(String email) {
        if (tokenStore.containsKey(email)) {
            long expireAt = tokenExpireTime.getOrDefault(email, 0L);
            if (Instant.now().getEpochSecond() < expireAt) {
                return tokenStore.get(email);
            }
        }
        return null;
    }
}
